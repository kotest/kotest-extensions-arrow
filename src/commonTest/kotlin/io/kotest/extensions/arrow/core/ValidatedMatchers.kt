package io.kotest.extensions.arrow.core

import io.kotest.property.Arb
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ValidatedMatchers : StringSpec({
  "shouldBeValid/ shouldBeInvalid return the correct value" {
    checkAll(Arb.validated(Arb.string(), Arb.int())) { v ->
      if (v.isInvalid) {
        v.shouldBeInvalid() shouldBe v.value
      } else {
        v.shouldBeValid() shouldBe v.value
      }
    }
  }

  "shouldBeValid/ shouldBeInvalid throw an AssertionError if the assertion is incorrect" {
    checkAll(Arb.validated(Arb.string(), Arb.int())) { v ->
      if (v.isInvalid) {
        shouldThrow<AssertionError> {
          v.shouldBeValid()
        }.message shouldContain "Expected Validated.Valid, but found Invalid with value"
      } else {
        shouldThrow<AssertionError> {
          v.shouldBeInvalid()
        }.message shouldContain "Expected Validated.Invalid, but found Valid with value"
      }
    }
  }
})
