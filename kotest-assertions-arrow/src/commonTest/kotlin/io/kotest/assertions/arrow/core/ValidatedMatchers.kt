package io.kotest.assertions.arrow.core

import arrow.core.Validated
import arrow.core.Validated.Valid
import io.kotest.property.Arb
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ValidatedMatchers : StringSpec({
  "shouldBeValid" {
    checkAll(Arb.int()) { v ->
      val valid = Valid(v)
      valid shouldBeValid v
      // smart cast test
      valid.value shouldBe v
    }
  }

  "shouldBeInvalid" {
    checkAll(Arb.int()) { v ->
      val invalid = Validated.Invalid(v)
      invalid shouldBeInvalid v
      // smart cast test
      invalid.value shouldBe v
    }
  }

  "shouldNotBeValid"{
    checkAll(
      Arb.bind(Arb.int(), Arb.int(), ::Pair)
        .filter { (a, b) -> a != b }
    ) { (a, b) ->
      Valid(a) shouldNotBeValid b
    }
  }

  "shouldNotBeInvalid"{
    checkAll(
      Arb.bind(Arb.int(), Arb.int(), ::Pair)
        .filter { (a, b) -> a != b }
    ) { (a, b) ->
      Validated.Invalid(a) shouldNotBeInvalid b
    }
  }
})
