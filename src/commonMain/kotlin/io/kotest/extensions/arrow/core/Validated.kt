package io.kotest.extensions.arrow.core

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import io.kotest.extensions.arrow.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.map
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * smart casts to [Validated.Valid] and fails with [failureMessage] otherwise
 * ```kotlin
 * import arrow.core.Validated
 * import arrow.core.computations.either
 * import arrow.core.valid
 * import arrow.core.invalid
 * import arrow.core.NonEmptyList
 * import arrow.typeclasses.Semigroup
 * import java.net.MalformedURLException
 * import java.net.URL
 * import io.kotest.extensions.core.shouldBeValid
 *
 * data class ConnectionParams(val url: URL, val port: Int)
 *
 * abstract class Read<A> {
 *  abstract fun read(s: String): A?
 *
 *  companion object {
 *   val urlRead: Read<URL> =
 *     object : Read<URL>() {
 *       override fun read(s: String): URL? =
 *         try {
 *           URL(s)
 *         } catch (_: MalformedURLException) {
 *           null
 *         }
 *       }
 *
 *   val intRead: Read<Int> =
 *    object : Read<Int>() {
 *     override fun read(s: String): Int? =
 *      if (s.matches(Regex("-?[0-9]+"))) s.toInt() else null
 *    }
 *  }
 * }
 *
 * sealed class ConfigError {
 *  data class MissingConfig(val field: String) : ConfigError()
 *  data class ParseConfig(val field: String) : ConfigError()
 * }
 *
 * data class Config(val map: Map<String, String>) {
 *   suspend fun <A> parse(read: Read<A>, key: String): ValidatedNel<ConfigError, A> =
 *     either<ConfigError, A> {
 *       val value: String = Validated.fromNullable(map[key]) {
 *         ConfigError.MissingConfig(key)
 *       }.bind()
 *       val readVal: A = Validated.fromNullable(read.read(value)) {
 *         ConfigError.ParseConfig(key)
 *       }.bind()
 *       readVal
 *     }.toValidatedNel()
 * }
 *
 * suspend fun main() {
 * //sampleStart
 * val config = Config(mapOf("wrong field" to "127.0.0.1", "port" to "8080", "url" to "https://kotest.io"))
 *
 * val params: ConnectionParams =
 *   config.parse(Read.urlRead, "url")
 *     .zip(
 *      Semigroup.nonEmptyList<ConfigError>(),
 *      config.parse(Read.intRead, "port")
 *     ) { url: URL, port -> ConnectionParams(url, port) }
 *     .shouldBeInvalid()
 * //sampleEnd
 *  println("connection = $params")
 * }
 * ```
 */
@OptIn(ExperimentalContracts::class)
public fun <E, A> Validated<E, A>.shouldBeValid(
  failureMessage: (Invalid<E>) -> String = { "Expected Validated.Valid, but found Invalid with value ${it.value}" }
): A {
  contract {
    returns() implies (this@shouldBeValid is Valid<A>)
  }
  return when (this) {
    is Valid -> value
    is Invalid -> throw AssertionError(failureMessage(this))
  }
}

public infix fun <E, A> Validated<E, A>.shouldBeValid(a: A): A =
  shouldBeValid().shouldBe(a)

/**
 * smart casts to [Validated.Invalid] and fails with [failureMessage] otherwise.
 * ```kotlin
 * import arrow.core.Validated
 * import arrow.core.computations.either
 * import arrow.core.zip
 * import arrow.core.valid
 * import arrow.core.invalid
 * import arrow.core.NonEmptyList
 * import arrow.typeclasses.Semigroup
 * import io.kotest.extensions.core.shouldBeInvalid
 *
 * data class ConnectionParams(val url: String, val port: Int)
 *
 * abstract class Read<A> {
 *  abstract fun read(s: String): A?
 *
 *  companion object {
 *
 *   val stringRead: Read<String> =
 *    object : Read<String>() {
 *     override fun read(s: String): String = s
 *    }
 *
 *   val intRead: Read<Int> =
 *    object : Read<Int>() {
 *     override fun read(s: String): Int? =
 *      if (s.matches(Regex("-?[0-9]+"))) s.toInt() else null
 *    }
 *  }
 * }
 *
 * sealed class ConfigError {
 *  data class MissingConfig(val field: String) : ConfigError()
 *  data class ParseConfig(val field: String) : ConfigError()
 * }
 *
 * data class Config(val map: Map<String, String>) {
 *   suspend fun <A> parse(read: Read<A>, key: String): ValidatedNel<ConfigError, A> =
 *     either<ConfigError, A> {
 *       val value: String = Validated.fromNullable(map[key]) {
 *         ConfigError.MissingConfig(key)
 *       }.bind()
 *       val readVal: A = Validated.fromNullable(read.read(value)) {
 *         ConfigError.ParseConfig(key)
 *       }.bind()
 *       readVal
 *     }.toValidatedNel()
 * }
 *
 * suspend fun main() {
 * //sampleStart
 * val config = Config(mapOf("wrong field" to "127.0.0.1", "port" to "not a number"))
 *
 * val failures: NonEmptyList<ConfigError> =
 *   config.parse(Read.stringRead, "url")
 *     .zip(
 *      Semigroup.nonEmptyList<ConfigError>(),
 *      config.parse(Read.intRead, "port")
 *     ) { url, port -> ConnectionParams(url, port) }
 *     .shouldBeInvalid()
 * //sampleEnd
 *  println("failures = $failures")
 * }
 * ```
 */
@OptIn(ExperimentalContracts::class)
public fun <E, A> Validated<E, A>.shouldBeInvalid(
  failureMessage: (Valid<A>) -> String = { "Expected Validated.Invalid, but found Valid with value ${it.value}" }
): E {
  contract {
    returns() implies (this@shouldBeInvalid is Invalid<E>)
  }
  return when (this) {
    is Valid -> throw AssertionError(failureMessage(this))
    is Invalid -> value
  }
}

public infix fun <E, A> Validated<E, A>.shouldBeInvalid(e: E): E =
  shouldBeInvalid().shouldBe(e)

public fun <E, A> Arb.Companion.validated(invalid: Arb<E>, valid: Arb<A>): Arb<Validated<E, A>> =
  choice(invalid.map(::Invalid), valid.map(::Valid))
