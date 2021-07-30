package io.kotest.assertions.arrow.option

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

@Deprecated("Use shouldBeSome directly")
fun beSome(): Matcher<Option<*>> =
  object : Matcher<Option<*>> {
    override fun test(value: Option<*>): MatcherResult =
      MatcherResult(value is Some, "$value should be Some", "$value should not be Some")
  }

@Deprecated(
  "Can be simplified",
  ReplaceWith(
    "shouldBeSome().shouldBe(a)",
    "io.kotest.assertions.arrow.core.shouldBeSome",
    "io.kotest.matchers.shouldBe"
  )
)
infix fun <A> Option<A>.shouldBeSome(a: A): Unit =
  should(beSome(a))

@Deprecated("Use shouldBeNone or shouldBeSome in combination with shouldNotBe instead")
infix fun <A> Option<A>.shouldNotBeSome(a: A): Unit =
  shouldNot(beSome(a))

@Deprecated("Use shouldBeSome directly")
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

@Deprecated("Use shouldBeSome", ReplaceWith("fn(shouldBeSome())"))
infix fun <A> Option<A>.shouldBeSomeFn(fn: (A) -> Unit): Unit =
  fn(shouldBeSome())

@Deprecated("Use shouldBeSome with shouldNotBe instead")
fun Option<Any?>.shouldNotBeNone(): Unit =
  shouldNot(beNone())

@Deprecated("Use shouldBeNone directly")
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
