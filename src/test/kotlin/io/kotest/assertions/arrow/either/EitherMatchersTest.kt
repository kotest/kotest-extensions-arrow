package io.kotest.assertions.arrow.either

import arrow.core.Either
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class EitherMatchersTest : WordSpec() {

  sealed class MyError {
    object Foo : MyError()
    object Boo : MyError()
  }

  init {

    "Either should beRight()" should {
      "test that the either is of type right" {
        Either.Right("boo").shouldBeRight()
        Either.Left("boo").shouldNotBeRight()
      }
      "use contracts to expose Right<*>" {
        val e = Either.Right("boo")
        e.shouldBeRight()
        e.value shouldBe "boo"
      }
    }

    "Either should beRight(fn)" should {
      "test that the either is of type right" {
        data class Person(val name: String, val location: String)
        Either.Right(Person("sam", "chicago")) shouldBeRight {
          it.name shouldBe "sam"
          it.location shouldBe "chicago"
        }
      }
    }

    "Either should beRight(value)" should {
      "test that an either is a right with the given value" {

        shouldThrow<AssertionError> {
          Either.Left("foo") should beRight("boo")
        }.message shouldBe "Either should be Right(boo) but was Left(foo)"

        shouldThrow<AssertionError> {
          Either.Right("foo") should beRight("boo")
        }.message shouldBe "Either should be Right(boo) but was Right(foo)"

        Either.Right("foo") shouldNotBeRight "boo"
        Either.Left("foo") shouldNotBeRight "foo"

        Either.Right("boo") should beRight("boo")
        Either.Right("boo") shouldBeRight "boo"
      }
    }

    "Either should beLeft()" should {
      "test that the either is of type left" {
        Either.Left("boo").shouldBeLeft()
        Either.Right("boo").shouldNotBeLeft()
      }
      "use contracts to expose Left<*>" {
        val e = Either.Left("boo")
        e.shouldBeLeft()
        e.value shouldBe "boo"
      }
    }

    "Either should beLeft(fn)" should {
      "test that the either is of type right" {
        data class Person(val name: String, val location: String)
        Either.Left(Person("sam", "chicago")) shouldBeLeft {
          it.name shouldBe "sam"
          it.location shouldBe "chicago"
        }
      }
    }

    "Either should beLeft(value)" should {
      "test that an either is a left with the given value" {

        shouldThrow<AssertionError> {
          Either.Right("foo") should beLeft("boo")
        }.message shouldBe "Either should be Left(boo) but was Right(foo)"

        shouldThrow<AssertionError> {
          Either.Left("foo") should beLeft("boo")
        }.message shouldBe "Either should be Left(boo) but was Left(foo)"

        shouldThrow<AssertionError> {
          Either.Left("foo") shouldNotBeLeft "foo"
        }.message shouldBe "Either should not be Left(foo)"

        Either.Left("boo") should beLeft("boo")
        Either.Left("boo") shouldBeLeft "boo"
        Either.Right("boo") shouldNotBeLeft "boo"
      }
    }

    "Either should beLeftOfType" should {
      "test that an either is a left have exactly the same type" {
        shouldThrow<AssertionError> {
          Either.Left(MyError.Boo).shouldBeLeftOfType<MyError.Foo>()
        }.message shouldBe "Either should be Left<${MyError.Foo::class.qualifiedName}> but was Left<${MyError.Boo::class.qualifiedName}>"

        Either.Left(MyError.Foo).shouldBeLeftOfType<MyError.Foo>()
        Either.Left(MyError.Boo).shouldBeLeftOfType<MyError.Boo>()

        Either.Left(MyError.Boo).shouldNotBeLeftOfType<MyError.Foo>()
        Either.Right("foo").shouldNotBeLeftOfType<MyError.Foo>()
      }
    }
  }
}
