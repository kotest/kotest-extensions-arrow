package io.kotest.assertions.arrow.core

import arrow.core.Either
import io.kotest.assertions.arrow.either.beLeft
import io.kotest.assertions.arrow.either.beRight
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeLeftOfType
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.assertions.arrow.either.shouldNotBeLeft
import io.kotest.assertions.arrow.either.shouldNotBeRight
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeTypeOf

class EitherMatchers : StringSpec({
  "shouldBeRight shouldNotBeRight" {
    Either.Right("boo").shouldBeRight()
    Either.Left("boo").shouldNotBeRight()
  }

  "use contracts to expose Right<*>" {
    Either.Right("boo").shouldBeRight("boo")
  }

  "Either.Right<Person> resolves to Right<Person>" {
    data class Person(val name: String, val location: String)
    Either.Right(Person("sam", "chicago")) shouldBeRight {
      it.name shouldBe "sam"
      it.location shouldBe "chicago"
    }
  }

  "Either should beRight(value)" {
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


  "Either should beLeftFn" {
    data class Person(val name: String, val location: String)

    Either.Left(Person("sam", "chicago")).shouldBeLeft { person: Person ->
      person.name shouldBe "sam"
      person.location shouldBe "chicago"
    }
  }

  "Either should beLeft(value)" {
    shouldThrow<AssertionError> {
      Either.Right("foo") should beLeft("boo")
    }.message shouldBe "Either should be Left(boo) but was Right(foo)"

    shouldThrow<AssertionError> {
      Either.Left("foo") should beLeft("boo")
    }.message shouldBe "Either should be Left(boo) but was Left(foo)"

    shouldThrow<AssertionError> {
      Either.Left("foo") shouldNotBeLeft "foo"
    }.message shouldBe "Either should not be Left(foo)"

    Either.Left("boo").shouldBeLeft().shouldBe("boo")
    Either.Right("boo") shouldNotBeLeft "boo"
  }

  "Either should beLeftOfType" {
    shouldThrow<AssertionError> {
      Either.Left(Error.ErrorB).shouldBeLeftOfType<Error.ErrorA>()
    }.message shouldBe "Either should be Left<${Error.ErrorA::class.qualifiedName}> but was Left<${Error.ErrorB::class.qualifiedName}>"

    Either.Left(Error.ErrorA).shouldBeLeft().shouldBeTypeOf<Error.ErrorA>()
    Either.Left(Error.ErrorB).shouldBeLeft().shouldBeTypeOf<Error.ErrorB>()

    Either.Left(Error.ErrorB).shouldBeLeft().shouldNotBeTypeOf<Error.ErrorA>()
    Either.Right("foo").shouldNotBeLeft<Error.ErrorA>()
  }
})

private sealed class Error {
  object ErrorA : Error()
  object ErrorB : Error()
}
