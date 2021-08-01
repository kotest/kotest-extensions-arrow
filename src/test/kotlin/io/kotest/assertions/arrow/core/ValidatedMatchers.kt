package io.kotest.assertions.arrow.core

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.invalid
import arrow.core.valid
import io.kotest.property.Arb
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.arrow.validation.shouldNotBeValid
import io.kotest.matchers.shouldNotBe
import io.kotest.property.arbitrary.int
import io.kotest.property.forAll

class ValidatedMatchers : StringSpec({
  "Validated shouldBeValid" {
    shouldThrow<AssertionError> {
      Invalid("error").shouldBeValid() shouldBe "error"
    }.message shouldBe "Expected Validated.Valid, but found Invalid with value error"

    Valid("ok").shouldBeValid() shouldBe "ok"
    shouldThrow<AssertionError> {
      Valid("ok").shouldBeValid() shouldNotBe "ok"
    }

    shouldThrow<AssertionError> {
      Valid("ok") shouldNotBeValid "ok"
    }.message shouldBe "Validated.Valid(ok) should not be Valid(ok)"
  }

  "Validated uses contracts to smart cast Valid" {
    forAll(Arb.int()) {
      val v = it.valid()
      v.shouldBeValid() == it
    }
  }

  "Validated uses contracts to smart cast Invalid" {
    forAll(Arb.int()) {
      val v = it.invalid()
      v.shouldBeInvalid() == it
    }
  }

  "Validated shouldBe Invalid" {
    shouldThrow<AssertionError> {
      Valid("foo").shouldBeInvalid()
    }.message shouldBe "Expected Validated.Invalid, but found Valid with value foo"

    Invalid("error").shouldBeInvalid() shouldBe "error"
    shouldThrow<AssertionError> {
      Invalid("error").shouldBeInvalid() shouldNotBe "error"
    }
    Invalid("error").shouldNotBeValid()
  }
})
