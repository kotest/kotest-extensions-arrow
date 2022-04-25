package io.kotest.assertions.arrow.fx.coroutines

import arrow.core.NonEmptyList
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.ValidatedNel
import arrow.core.continuations.AtomicRef
import arrow.core.continuations.update
import arrow.core.identity
import arrow.core.invalidNel
import arrow.core.traverse
import arrow.core.valid
import arrow.fx.coroutines.ExitCase
import io.kotest.core.TestConfiguration
import arrow.fx.coroutines.Platform
import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.bracketCase
import arrow.fx.coroutines.continuations.ResourceScope
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.Dispatchers

public suspend fun <A> Resource<A>.shouldBeResource(a: A): A =
  use { it shouldBe a }

public suspend fun <A> Resource<A>.shouldBeResource(expected: Resource<A>): A =
  zip(expected).use { (a, b) -> a shouldBe b }

/**
 * ensures that all finalizers of [resource] are invoked for the given [TestConfiguration],
 * by registering a listener.
 *
 * ```kotlin
 * interface Database { // given a Dependency
 *   fun isRunning(): Boolean
 *   suspend fun version(): String?
 * }
 *
 * // and a Way to create it, here with Hikari and SqlDelight
 * fun database(hikari: HikariDataSource): Database =
 *   object : Database {
 *     override fun isRunning(): Boolean = hikari.isRunning
 *     override suspend fun version(): String? =
 *       hikari.queryOneOrNull("SHOW server_version;") { string() }
 *    }
 *
 * fun hikari(): Resource<HikariDataSource> =
 *   Resource.fromCloseable {
 *     HikariDataSource(
 *      HikariConfig().apply {
 *       // config settings
 *      }
 *     )
 *   }
 * fun sqlDelight(hikariDataSource: HikariDataSource): Resource<SqlDelight> =
 *   Resource.fromCloseable { hikariDataSource.asJdbcDriver() }.map { driver -> SqlDelight(driver) }
 *
 * // we can define a Database Resource
 * fun database(): Resource<Database> =
 *  hikari().flatMap { hikari ->
 *   sqlDelight(hikari).map { sqlDelight -> database(hikari) }
 *  }
 *
 * // and define Tests with our backend Application, here with Ktor
 * class HealthCheckSpec :
 *   StringSpec({
 *     val database: Database = resource(database())
 *
 *     "healthy" {
 *       testApplication {
 *         application { app(database) }
 *         val response = client.get("/health")
 *         response.status shouldBe HttpStatusCode.OK
 *         response.bodyAsText() shouldBe Json.encodeToString(HealthCheck("14.1"))
 *       }
 *     }
 *   })
 * ```
 */
public fun <A> TestConfiguration.resource(resource: Resource<A>): ReadOnlyProperty<Any?, A>  =
  TestResource(resource).also(::listener)

private class TestResource<A>(private val resource: Resource<A>) :
  TestListener, ResourceScope, ReadOnlyProperty<Any?, A> {
  private val value: AtomicRef<Option<A>> = AtomicRef(None)
  private val finalizers: AtomicRef<List<suspend (ExitCase) -> Unit>> = AtomicRef(emptyList())

  suspend fun value(): A =
    resource.bind()

  @Suppress("DEPRECATION")
  override suspend fun <A> Resource<A>.bind(): A =
    when (this) {
      is Resource.Dsl -> dsl.invoke(this@TestResource)
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
      is Resource.Bind<*, *> -> {
        val dsl: suspend ResourceScope.() -> A = {
          val any = source.bind()
          @Suppress("UNCHECKED_CAST") val ff = f as (Any?) -> Resource<A>
          ff(any).bind()
        }
        dsl(this@TestResource)
      }
      is Resource.Defer -> resource().bind()
    }

  override fun getValue(thisRef: Any?, property: KProperty<*>): A =
    value.modify {
      when (it) {
        None -> runBlocking(Dispatchers.Default) { resource.bind() }.let { a -> Pair(Some(a), a) }
        is Some -> Pair(it, it.value)
      }
    }

  override suspend fun beforeSpec(spec: Spec) {
    super.beforeSpec(spec)
    value.modify {
      when (it) {
        None ->
          resource.bind().let { a ->
            println("Initialised in beforeSpec")
            Pair(Some(a), a)
          }
        is Some -> Pair(it, it.value)
      }
    }
  }

  override suspend fun afterSpec(spec: Spec) {
    super.afterSpec(spec)
    finalizers.get().cancelAll(ExitCase.Completed)
  }
}

private inline fun <A, B> AtomicRef<A>.modify(function: (A) -> Pair<A, B>): B {
  while (true) {
    val cur = get()
    val (upd, res) = function(cur)
    if (compareAndSet(cur, upd)) return res
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
  traverse { f -> catchNel { f(exitCase) } }
    .fold(
      {
        if (first != null) Platform.composeErrors(NonEmptyList(first, it))
        else Platform.composeErrors(it)
      },
      { first }
    )
