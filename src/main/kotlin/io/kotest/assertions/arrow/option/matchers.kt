package io.kotest.assertions.arrow.option

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Option<*>.shouldBeSome() {
  contract {
    returns() implies (this@shouldBeSome is Some<*>)
  }
  should(beSome())
}

fun beSome() =
  object : Matcher<Option<*>> {
    override fun test(value: Option<*>): MatcherResult =
      MatcherResult(value is Some, "$value should be Some", "$value should not be Some")
  }

infix fun <A> Option<A>.shouldBeSome(a: A): Unit =
  should(beSome(a))

infix fun <A> Option<A>.shouldNotBeSome(a: A): Unit =
  shouldNot(beSome(a))

fun <A> beSome(a: A): Matcher<Option<A>> =
  object : Matcher<Option<A>> {
    override fun test(value: Option<A>): MatcherResult {
      return when (value) {
        is None -> {
          MatcherResult(false, "Option should be Some($a) but was None", "")
        }
        is Some -> {
          if (value.value == a)
            MatcherResult(true, "Option should be Some($a)", "Option should not be Some($a)")
          else
            MatcherResult(false, "Option should be Some($a) but was Some(${value.value})", "")
        }
      }
    }
  }

@OptIn(ExperimentalContracts::class)
infix fun <A> Option<A>.shouldBeSome(fn: (A) -> Unit) {
  this.shouldBeSome()
  fn((this.value as A))
}

fun Option<Any?>.shouldBeNone(): Unit =
  should(beNone())

fun Option<Any?>.shouldNotBeNone(): Unit =
  shouldNot(beNone())

fun beNone(): Matcher<Option<Any?>> =
  object : Matcher<Option<Any?>> {
    override fun test(value: Option<Any?>): MatcherResult {
      return when (value) {
        is None -> {
          MatcherResult(true, "Option should be None", "Option should not be None")
        }
        is Some -> {
          MatcherResult(false, "Option should be None but was Some(${value.value})", "")
        }
      }
    }
  }
