package core

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class EitherMatchers : StringSpec({
  "shouldBeRight shouldBeLeft" {
    checkAll(Arb.either(Arb.string(), Arb.int())) {
      if (it.isRight()) it.shouldBeRight() shouldBe it.value else it.shouldBeLeft() shouldBe it.value
    }
  }
})
