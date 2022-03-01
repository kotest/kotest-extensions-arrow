package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.singleThreadContext
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class JvmPredefTest : StringSpec({
  "shift" {
    checkAll(Arb.string(), Arb.string()) { a, b ->
      val t0 = threadName.invoke()

      Resource.singleThreadContext(a)
        .zip(Resource.singleThreadContext(b))
        .use { (ui, io) ->
          t0 shouldBe threadName.invoke()

          ui.shift()
          threadName.invoke() shouldBe a

          io.shift()
          threadName.invoke() shouldBe b
        }
    }
  }
})
