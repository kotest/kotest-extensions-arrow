package io.kotest.assertions.arrow.fx.coroutines

import arrow.continuations.generic.AtomicRef
import arrow.continuations.generic.update
import arrow.core.NonEmptyList
import arrow.core.ValidatedNel
import arrow.core.identity
import arrow.core.invalidNel
import arrow.core.traverseValidated
import arrow.core.valid
import arrow.fx.coroutines.ExitCase
import io.kotest.core.TestConfiguration
import arrow.fx.coroutines.Platform
import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.bracketCase
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec

public suspend fun <A> Resource<A>.shouldBeResource(a: A): A =
  use { it shouldBe a }

public suspend fun <A> Resource<A>.shouldBeResource(expected: Resource<A>): A =
  zip(expected).use { (a, b) -> a shouldBe b }

/**
 * ensures that all finalizers of [resource] are invoked for the given [TestConfiguration],
 * by registering a listener.
 */
public suspend inline fun <A> TestConfiguration.resource(resource: Resource<A>): A =
  TestResource(resource).also(this::listener).bind()

@PublishedApi
internal class TestResource<A>(private val resource: Resource<A>) : TestListener {
  private val finalizers: AtomicRef<List<suspend (ExitCase) -> Unit>> = AtomicRef(emptyList())

  @PublishedApi
  internal suspend fun bind(): A =
    resource.bind()

  // Remove once resource computation block is available
  private suspend fun <B> Resource<B>.bind(): B =
    when (this) {
      is Resource.Bind<*, *> -> {
        val any = source.bind()

        @Suppress("UNCHECKED_CAST") val ff = f as ((Any?) -> Resource<B>)
        ff(any).bind()
      }
      is Resource.Allocate ->
        bracketCase(
          {
            val a = acquire()
            val finalizer: suspend (ExitCase) -> Unit = { ex: ExitCase -> release(a, ex) }
            finalizers.update { it + finalizer }
            a
          },
          ::identity,
          { a, ex ->
            // Only if ExitCase.Failure, or ExitCase.Cancelled during acquire we cancel
            // Otherwise we've saved the finalizer, and it will be called from somewhere else.
            if (ex != ExitCase.Completed) {
              val e = finalizers.get().cancelAll(ex)
              val e2 = runCatching { release(a, ex) }.exceptionOrNull()
              Platform.composeErrors(e, e2)?.let { throw it }
            }
          }
        )
      is Resource.Defer -> resource().bind()
    }

  override suspend fun afterSpec(spec: Spec) {
    super.afterSpec(spec)
    finalizers.get().cancelAll(ExitCase.Completed)
  }
}

private inline fun <A> catchNel(f: () -> A): ValidatedNel<Throwable, A> =
  try {
    f().valid()
  } catch (e: Throwable) {
    e.invalidNel()
  }

private suspend fun List<suspend (ExitCase) -> Unit>.cancelAll(
  exitCase: ExitCase,
  first: Throwable? = null
): Throwable? =
  traverseValidated { f -> catchNel { f(exitCase) } }
    .fold(
      {
        if (first != null) Platform.composeErrors(NonEmptyList(first, it))
        else Platform.composeErrors(it)
      },
      { first }
    )
