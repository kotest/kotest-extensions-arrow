package io.kotest.assertions.arrow.core

import arrow.core.Option
import io.kotest.assertions.arrow.option.beNone
import io.kotest.assertions.arrow.option.beSome
import io.kotest.assertions.arrow.option.shouldBeSome
import io.kotest.assertions.arrow.option.shouldNotBeNone
import io.kotest.assertions.arrow.option.shouldNotBeSome
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class OptionMatchers : StringSpec({
  "Option.shouldBeSome()"  {
    val o = Option("foo").shouldBeSome()
    o shouldBe "foo"
  }

  "Option shouldBe some(value)"  {
    shouldThrow<AssertionError> {
      val foo = Option.fromNullable<String>(null).shouldBeSome()
      foo.shouldBe("foo")
    }.message shouldBe "Expected Some, but found None"

    shouldThrow<AssertionError> {
      Option.fromNullable("boo").shouldBeSome("foo")
    }.message shouldBe "Option should be Some(foo) but was Some(boo)"

    val some = Option.fromNullable("foo").shouldBeSome()
    some.shouldBe("foo")

    shouldThrow<AssertionError> {
      some shouldNotBe "foo"
    }
  }

  "Option shouldNotBeSome(value)" {
    val option = Option.fromNullable("foo")
    option shouldNotBeSome "bar"
  }

  "Option shouldBe none()" {

    shouldThrow<AssertionError> {
      Option.fromNullable("foo") shouldBe beNone()
    }.message shouldBe "Option should be None but was Some(foo)"

    Option.fromNullable<String>(null) shouldBe beNone()
    Option.fromNullable<String>(null).shouldBeNone()
  }

  "Option shouldNotBe none()" {
    val option = Option.fromNullable("foo")

    option.shouldNotBeNone()
  }
})
