package io.kotest.assertions.arrow.core

import arrow.typeclasses.Monoid
import io.kotest.assertions.arrow.core.laws.MonoidLaws
import io.kotest.assertions.arrow.laws.testLaws
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool

class BooleanTest : StringSpec({
  "Boolean obeys MonoidLaws" {
    testLaws(
      MonoidLaws.laws(Monoid.boolean(), Arb.bool())
    )
  }
})
