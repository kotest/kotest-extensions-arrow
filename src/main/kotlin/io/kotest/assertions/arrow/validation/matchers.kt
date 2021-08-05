package io.kotest.assertions.arrow.validation

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import io.kotest.assertions.arrow.core.shouldBeInvalid
import io.kotest.assertions.arrow.core.shouldBeValid
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Deprecated(
  "Use shouldBeValid from core",
  ReplaceWith("shouldBeValid()", "io.kotest.assertions.arrow.core.shouldBeValid")
)
@OptIn(ExperimentalContracts::class)
fun Validated<*, *>.shouldBeValid() {
  contract {
    returns() implies (this@shouldBeValid is Valid<*>)
  }
  shouldBeValid()
}

@Deprecated(
  "Use shouldBeInvalid from core",
  ReplaceWith("shouldBeInvalid()", "io.kotest.assertions.arrow.core.shouldBeInvalid")
)
fun <A> Validated<*, A>.shouldNotBeValid(): Unit =
  shouldNot(beValid())

@Deprecated(
  "Convenience function is deprecated",
  ReplaceWith(
    "shouldBeValid().shouldBe(b)",
    "io.kotest.matchers.shouldBe",
    "io.kotest.assertions.arrow.core.shouldBeValid"
  )
)
infix fun <A> Validated<*, A>.shouldBeValid(value: A): Unit =
  should(beValid(value))

@Deprecated(
  "Convenience function is deprecated",
  ReplaceWith(
    "shouldBeValid().shouldNotBe(b)",
    "io.kotest.matchers.shouldNotBe",
    "io.kotest.assertions.arrow.core.shouldBeValid"
  )
)
infix fun <A> Validated<*, A>.shouldNotBeValid(value: A): Unit =
  shouldNot(beValid(value))

@Deprecated("Convenience function is deprecated us shouldBeValid from core")
infix fun <A> Validated<*, A>.shouldBeValid(fn: (Valid<A>) -> Unit) {
  this.shouldBeValid()
  fn(this)
}

@Deprecated("Use shouldBeValid from core directly")
fun <A> beValid(): Matcher<Validated<*, A>> =
  object : Matcher<Validated<*, A>> {
    override fun test(value: Validated<*, A>): MatcherResult =
      MatcherResult(value is Valid, "$value should be Valid", "$value should not be Valid")
  }

@Deprecated("Use shouldBeValid and shouldBe directly")
fun <A> beValid(a: A): Matcher<Validated<*, A>> =
  object : Matcher<Validated<*, A>> {
    override fun test(value: Validated<*, A>): MatcherResult =
      MatcherResult(value == Valid(a), "$value should be Valid($a)", "$value should not be Valid($a)")
  }

@Deprecated(
  "Use shouldBeInvalid from core",
  ReplaceWith("shouldBeInvalid()", "io.kotest.assertions.arrow.core.shouldBeInvalid")
)
@OptIn(ExperimentalContracts::class)
fun Validated<*, *>.shouldBeInvalid(): Unit {
  contract {
    returns() implies (this@shouldBeInvalid is Validated.Invalid<*>)
  }
  shouldBeInvalid()
}

@Deprecated(
  "Convenience function is deprecated",
  ReplaceWith(
    "shouldBeInvalid().shouldBe(a)",
    "io.kotest.matchers.shouldBe",
    "io.kotest.assertions.arrow.core.shouldBeValid"
  )
)
infix fun <E> Validated<E, *>.shouldBeInvalid(value: E): Unit =
  should(beInvalid(value))

@Deprecated("Use shouldBeInvalid instead")
infix fun <E> Validated<E, *>.shouldBeInvalid(fn: (Invalid<E>) -> Unit): Unit {
  this.shouldBeInvalid()
  fn(this)
}

@Deprecated("Use shouldBeInvalid directly")
fun <E> beInvalid(): Matcher<Validated<E, *>> =
  object : Matcher<Validated<E, *>> {
    override fun test(value: Validated<E, *>): MatcherResult =
      MatcherResult(value is Invalid, "$value should be Invalid", "$value should not be Invalid")
  }

@Deprecated("Use shouldBeInvalid with shouldBe or shouldNotBe directly")
fun <E> beInvalid(e: E): Matcher<Validated<E, *>> =
  object : Matcher<Validated<E, *>> {
    override fun test(value: Validated<E, *>): MatcherResult =
      MatcherResult(value == Invalid(e), "$value should be Invalid($e)", "$value should not be Invalid($e)")
  }
