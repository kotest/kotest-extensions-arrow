package io.kotest.assertions.arrow.option

import arrow.core.Option
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class OptionMatchersTest : WordSpec() {

  init {

    "Option.shouldBeSome()" should {
      "use contracts" {
        val o = Option("foo")
        o.shouldBeSome()
        o.value shouldBe "foo"
      }
    }

    "Option shouldBe some(value)" should {
      "test that an option is a Some with the given value" {

        shouldThrow<AssertionError> {
          Option.fromNullable<String>(null) shouldBe beSome("foo")
        }.message shouldBe "Option should be Some(foo) but was None"

        shouldThrow<AssertionError> {
           Option.fromNullable<String>(null) shouldBeSome "foo"
        }.message shouldBe "Option should be Some(foo) but was None"

        shouldThrow<AssertionError> {
          Option.fromNullable("boo") shouldBe beSome("foo")
        }.message shouldBe "Option should be Some(foo) but was Some(boo)"

        val option = Option.fromNullable("foo")
        option shouldBe beSome("foo")
        option shouldBeSome "foo"

        option shouldBeSome { it shouldBe "foo" }
        shouldThrow<AssertionError> {
           option shouldBeSome { it shouldNotBe "foo" }
        }
      }
    }

    "Option shouldNotBe some(value)" should {
      "test that an option is not a Some with the given value" {

        val option = Option.fromNullable("foo")
        option shouldNotBe beSome("bar")
        option shouldNotBeSome "bar"
      }
    }

    "Option shouldBe none()" should {
      "test that an option is a None" {

        shouldThrow<AssertionError> {
          Option.fromNullable("foo") shouldBe beNone()
        }.message shouldBe "Option should be None but was Some(foo)"

        Option.fromNullable<String>(null) shouldBe beNone()
        Option.fromNullable<String>(null).shouldBeNone()
      }
    }

    "Option shouldNotBe none()" should {
      "test that an option is not a None" {
        val option = Option.fromNullable("foo")

        option shouldNotBe beNone()
        option.shouldNotBeNone()
      }
    }
  }
}
