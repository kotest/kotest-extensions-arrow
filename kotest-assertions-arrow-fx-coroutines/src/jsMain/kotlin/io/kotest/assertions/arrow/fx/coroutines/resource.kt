package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.Resource
import io.kotest.core.TestConfiguration
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise

/**
 * ensures that all finalizers of [resource] are invoked for the given [TestConfiguration],
 * by registering a listener.
 *
 * Here an example with [Hikari](https://github.com/brettwooldridge/HikariCP).
 *
 * ```kotlin
 * import arrow.fx.coroutines.Resource
 * import com.zaxxer.hikari.HikariDataSource
 *
 * fun hikari(config: HikariConfig): Resource<HikariDataSource> =
 *   Resource.fromCloseable { HikariDataSource(config) }
 *
 * class DependencyGraphSpec :
 *   StringSpec({
 *     val config =
 *        HikariConfig().apply {
 *          jdbcUrl = "myJdbcUrl"
 *          driverClassName = "myDriverClass"
 *        }
 *
 *     val datasource: HikariDataSource = resource(hikari())
 *
 *     "Database isRunning" {
 *       datasource.isRunning.shouldBeTrue()
 *     }
 *   })
 * ```
 */
@OptIn(DelicateCoroutinesApi::class)
public fun <A> TestConfiguration.resource(resource: Resource<A>): dynamic =
  GlobalScope.promise(Dispatchers.Default) { suspendResource(resource) }
