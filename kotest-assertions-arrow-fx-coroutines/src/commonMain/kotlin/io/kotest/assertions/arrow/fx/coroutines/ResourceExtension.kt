package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.Resource
import io.kotest.core.extensions.MountableExtension
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import io.kotest.core.test.isRootTest

public enum class LifecycleMode {
  Spec, EveryTest, Leaf, Root
}

public fun <A> Resource<A>.extension(lifecycleMode: LifecycleMode = LifecycleMode.Spec): ResourceExtension<A> =
  ResourceExtension(this, lifecycleMode)

/**
 * Ensures that all finalizers of [resource] are invoked for the specified [LifecycleMode].
 * Installing this [MountableExtension] returns a [TestResource]
 * from which you can extract initialized [A] using [TestResource.invoke] or [TestResource.get].
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
 * and it will result in [TestResource.AlreadyClosedException]
 *
 * @param lifecycleMode allows installing the [Resource] for other scopes besides the default [LifecycleMode.Spec].
 */
public class ResourceExtension<A>(
  public val resource: Resource<A>, public val lifecycleMode: LifecycleMode = LifecycleMode.Spec
) : MountableExtension<A, TestResource<A>>, TestListener, AfterSpecListener {
  private lateinit var testResource: DefaultTestResource<A>

  override fun mount(configure: A.() -> Unit): TestResource<A> {
    testResource = DefaultTestResource(resource, configure)
    return testResource
  }

  override suspend fun beforeSpec(spec: Spec) {
    super.beforeSpec(spec)
    if (lifecycleMode == LifecycleMode.Spec) {
      testResource.init()
    }
  }

  override suspend fun afterSpec(spec: Spec) {
    if (lifecycleMode == LifecycleMode.Spec) {
      testResource.release()
    }
    testResource.close()
  }

  override suspend fun beforeAny(testCase: TestCase) {
    val every = lifecycleMode == LifecycleMode.EveryTest
    val root = lifecycleMode == LifecycleMode.Root && testCase.isRootTest()
    val leaf = lifecycleMode == LifecycleMode.Leaf && testCase.type == TestType.Test
    if (every || root || leaf) {
      testResource.init()
    }
  }

  override suspend fun afterAny(testCase: TestCase, result: TestResult) {
    val every = lifecycleMode == LifecycleMode.EveryTest
    val root = lifecycleMode == LifecycleMode.Root && testCase.isRootTest()
    val leaf = lifecycleMode == LifecycleMode.Leaf && testCase.type == TestType.Test
    if (every || root || leaf) {
      testResource.release()
    }
  }
}
