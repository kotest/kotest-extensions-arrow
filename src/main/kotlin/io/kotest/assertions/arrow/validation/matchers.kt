package io.kotest.assertions.arrow.validation

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Validated<*, *>.shouldBeValid() {
  contract {
    returns() implies (this@shouldBeValid is Valid<*>)
  }
  should(beValid())
}

fun <A> Validated<*, A>.shouldNotBeValid() =
  shouldNot(beValid())

infix fun <A> Validated<*, A>.shouldBeValid(value: A): Unit =
  should(beValid(value))

infix fun <A> Validated<*, A>.shouldNotBeValid(value: A): Unit =
  shouldNot(beValid(value))

infix fun <A> Validated<*, A>.shouldBeValid(fn: (Valid<A>) -> Unit) {
  this.shouldBeValid()
  fn(this as Valid<A>)
}

fun <A> beValid() =
  object : Matcher<Validated<*, A>> {
    override fun test(value: Validated<*, A>): MatcherResult =
      MatcherResult(value is Valid, "$value should be Valid", "$value should not be Valid")
  }

fun <A> beValid(a: A) =
  object : Matcher<Validated<*, A>> {
    override fun test(value: Validated<*, A>): MatcherResult =
      MatcherResult(value == Valid(a), "$value should be Valid($a)", "$value should not be Valid($a)")
  }

@OptIn(ExperimentalContracts::class)
fun Validated<*, *>.shouldBeInvalid() {
  contract {
    returns() implies (this@shouldBeInvalid is Validated.Invalid<*>)
  }
  should(beInvalid())
}

infix fun <A> Validated<*, A>.shouldBeInvalid(value: A): Unit =
  should(beInvalid(value))

infix fun <A> Validated<A, *>.shouldBeInvalid(fn: (Invalid<A>) -> Unit): Unit {
  this.shouldBeInvalid()
  fn(this as Invalid<A>)
}

fun <A> beInvalid() =
  object : Matcher<Validated<*, A>> {
    override fun test(value: Validated<*, A>): MatcherResult =
      MatcherResult(value is Invalid, "$value should be Invalid", "$value should not be Invalid")
  }

fun <A> beInvalid(a: A) =
  object : Matcher<Validated<*, A>> {
    override fun test(value: Validated<*, A>): MatcherResult =
      MatcherResult(value == Invalid(a), "$value should be Invalid($a)", "$value should not be Invalid($a)")
  }
