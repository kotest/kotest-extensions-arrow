package io.kotest.assertions.arrow.fx.coroutines.internal

import arrow.core.continuations.AtomicRef
import arrow.core.continuations.update
import arrow.core.identity
import arrow.core.prependTo
import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.Platform
import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.bracketCase
import arrow.fx.coroutines.continuations.ResourceScope
import arrow.fx.coroutines.continuations.resource

// This is going to become a public API in Arrow Fx for inter-op cases like this
internal suspend fun <A> Resource<A>.allocatedCase(): Pair<A, suspend (ExitCase) -> Unit> {
  val finalizers: AtomicRef<List<suspend (ExitCase) -> Unit>> = AtomicRef(emptyList())
  val effect = ResourceScopeImpl(finalizers)

  return when (this) {
    is Resource.Defer -> resource().allocatedCase()
    is Resource.Allocate -> resource { bind() }.allocatedCase()
    is Resource.Bind<*, *> -> resource { bind() }.allocatedCase()
    is Resource.Dsl -> Pair(dsl.invoke(effect)) { finalizers.get().cancelAll(it)?.let { throw it } }
  }
}

private class ResourceScopeImpl(
  val finalizers: AtomicRef<List<suspend (ExitCase) -> Unit>>
) : ResourceScope {

  override suspend fun <A> Resource<A>.bind(): A =
    when (this) {
      is Resource.Dsl -> dsl.invoke(this@ResourceScopeImpl)
      is Resource.Allocate -> bracketCase({
        val a = acquire()
        val finalizer: suspend (ExitCase) -> Unit = { ex: ExitCase -> release(a, ex) }
        finalizers.update { finalizer prependTo it }
        a
      }, ::identity, { a, ex ->
        // Only if ExitCase.Failure, or ExitCase.Cancelled during acquire we cancel
        // Otherwise we've saved the finalizer, and it will be called from somewhere else.
        if (ex != ExitCase.Completed) {
          val e = finalizers.get().cancelAll(ex)
          val e2 = kotlin.runCatching { release(a, ex) }.exceptionOrNull()
          Platform.composeErrors(e, e2)?.let { throw it }
        }
      })

      is Resource.Bind<*, *> -> {
        val dsl: suspend ResourceScope.() -> A = {
          val any = source.bind()
          val ff = f as (Any?) -> Resource<A>
          ff(any).bind()
        }
        dsl(this@ResourceScopeImpl)
      }

      is Resource.Defer -> resource().bind()
    }
}

private suspend fun List<suspend (ExitCase) -> Unit>.cancelAll(
  exitCase: ExitCase,
  first: Throwable? = null
): Throwable? =
  fold(first) { acc, finalizer ->
    val other = kotlin.runCatching { finalizer(exitCase) }.exceptionOrNull()
    other?.let {
      acc?.apply { addSuppressed(other) } ?: other
    } ?: acc
  }
