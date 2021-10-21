package io.kotest.property.arrow.core

import arrow.typeclasses.Monoid
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.arrow.laws.testLaws

class MonoidLawTests : StringSpec({
  "Boolean, Int, String obeys MonoidLaws" {
    testLaws(
      MonoidLaws.laws(Monoid.boolean(), Arb.boolean()),
      MonoidLaws.laws(Monoid.int(), Arb.int()),
      MonoidLaws.laws(Monoid.string(), Arb.string())
    )
  }
})
