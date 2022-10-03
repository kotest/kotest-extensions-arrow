package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.Resource
import io.kotest.core.extensions.LazyMaterialized
import io.kotest.core.extensions.LazyMountableExtension
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import io.kotest.core.test.isRootTest
import io.kotest.mpp.atomics.AtomicReference
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.DelicateCoroutinesApi

public enum class LifecycleMode {
  Spec, EveryTest, Leaf, Root
}

public fun <A> Resource<A>.extension(lifecycleMode: LifecycleMode = LifecycleMode.Spec): ResourceExtension<A> =
  ResourceExtension(this, lifecycleMode)

/**
 * Ensures that all finalizers of [resource] are invoked for the specified [LifecycleMode].
 * Installing this [LazyMountableExtension] returns a [LazyMaterialized]
 * from which you can extract initialized [A] using [LazyMaterialized.get].
 *
 * ```kotlin
 * class Connection(val name: String) {
 *   fun open() = println("Open $name")
 *   fun compute() = "Result of $name"
 *   fun release(ex: ExitCase) = println("Closing $name with $ex")
 * }
 *
 * fun connection(name: String): Resource<Connection> =
 *   Resource(
 *     { Resource(name) },
 *     { conn, ex -> conn.release(ex) }
 *   )
 *
 * class ResourceSpec : StringSpec({
 *   val conn: TestResource<Connection> =
 *     install(ResourceExtension(connection("DB")))
 *
 *   "invoke" {
 *     conn().compute() shouldBe "Result of DB"
 *   }
 *
 *   "get" {
 *     conn.get().compute() shouldBe "Result of DB"
 *   }
 *
 *   afterSpec {
 *     shouldThrow<TestResource.AlreadyClosedException> {
 *       conn.get()
 *     }
 *   }
 * })
 * ```
 *
 * `conn` cannot be used during or after `afterSpec` since at that point the resource is already released,
 * and it will result in [IllegalStateException]
 *
 * @param lifecycleMode allows installing the [Resource] for other scopes besides the default [LifecycleMode.Spec].
 */
public class ResourceExtension<A>(
  public val resource: Resource<A>, public val lifecycleMode: LifecycleMode = LifecycleMode.Spec,
) : LazyMountableExtension<A, A>, TestListener, AfterSpecListener {
  
  private var underlying: ResourceLazyMaterialized<A>? = null
  
  override fun mount(configure: A.() -> Unit): LazyMaterialized<A> =
    ResourceLazyMaterialized(resource, configure).also {
      underlying = it
    }
  
  override suspend fun beforeSpec(spec: Spec) {
    super.beforeSpec(spec)
    if (lifecycleMode == LifecycleMode.Spec) {
      underlying?.init()
    }
  }
  
  override suspend fun afterSpec(spec: Spec) {
    if (lifecycleMode == LifecycleMode.Spec) {
      underlying?.release()
    }
    underlying?.close()
  }
  
  override suspend fun beforeAny(testCase: TestCase) {
    val every = lifecycleMode == LifecycleMode.EveryTest
    val root = lifecycleMode == LifecycleMode.Root && testCase.isRootTest()
    val leaf = lifecycleMode == LifecycleMode.Leaf && testCase.type == TestType.Test
    if (every || root || leaf) {
      underlying?.init()
    }
  }
  
  override suspend fun afterAny(testCase: TestCase, result: TestResult) {
    val every = lifecycleMode == LifecycleMode.EveryTest
    val root = lifecycleMode == LifecycleMode.Root && testCase.isRootTest()
    val leaf = lifecycleMode == LifecycleMode.Leaf && testCase.type == TestType.Test
    if (every || root || leaf) {
      underlying?.release()
    }
  }
}

internal class ResourceLazyMaterialized<A>(
  private val resource: Resource<A>,
  private val configure: A.() -> Unit = {},
) : LazyMaterialized<A> {
  
  sealed interface State<out A> {
    object Empty : State<Nothing>
    object Closed : State<Nothing>
    data class Loading<A>(
      val acquiring: CompletableDeferred<A> = CompletableDeferred(),
      val finalizers: CompletableDeferred<suspend (ExitCase) -> Unit> = CompletableDeferred(),
    ) : State<A>
    
    data class Done<A>(val value: A, val finalizers: suspend (ExitCase) -> Unit) : State<A>
  }
  
  private val state = AtomicReference<State<A>>(State.Empty)
  
  override suspend fun get(): A = init()
  
  @OptIn(DelicateCoroutinesApi::class)
  tailrec suspend fun init(): A = when (val current = state.value) {
    is State.Done -> current.value
    is State.Loading -> current.acquiring.await()
    is State.Closed -> throw IllegalStateException("Resource already closed and cannot be re-opened.")
    State.Empty -> {
      val loading = State.Loading<A>()
      if (state.compareAndSet(State.Empty, loading)) {
        val (res, fin) = resource.allocated()
          .let { (acquire, finalizer) ->
            val a = acquire()
            @Suppress("NAME_SHADOWING")
            val finalizer: suspend (ExitCase) -> Unit = { finalizer(a, it) }
            Pair(a, finalizer)
          }
        state.value = State.Done(res, fin)
        loading.acquiring.complete(res.also(configure))
        loading.finalizers.complete(fin)
        res
      } else init()
    }
  }
  
  tailrec suspend fun release(): Unit = when (val current = state.value) {
    State.Empty -> Unit
    is State.Done -> if (state.compareAndSet(current, State.Empty)) current.finalizers(ExitCase.Completed)
    else release()
    
    is State.Loading -> if (state.compareAndSet(current, State.Empty)) current.finalizers.await()
      .invoke(ExitCase.Completed)
    else release()
    
    State.Closed -> Unit
  }
  
  fun close() {
    state.value = State.Closed
  }
}
