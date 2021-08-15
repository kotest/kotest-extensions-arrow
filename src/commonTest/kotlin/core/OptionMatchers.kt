package core

import arrow.core.Option
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
      Option.fromNullable("boo").shouldBeSome() shouldBe "foo"
    }.message shouldBe "expected:<\"foo\"> but was:<\"boo\">"

    val some = Option.fromNullable("foo").shouldBeSome()
    some.shouldBe("foo")

    shouldThrow<AssertionError> {
      some shouldNotBe "foo"
    }
  }

  "Option shouldNotBeSome(value)" {
    val option = Option.fromNullable("foo")
    option.shouldBeSome() shouldNotBe "bar"
  }

  "Option shouldBe none()" {
    shouldThrow<AssertionError> {
      Option.fromNullable("foo").shouldBeNone()
    }.message shouldBe "Expected None, but found Some with value foo"

    Option.fromNullable<String>(null).shouldBeNone()
  }
})
