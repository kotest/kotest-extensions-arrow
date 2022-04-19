package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.Resource
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlinx.coroutines.CompletableDeferred

class ResourceSpec : StringSpec({
  "resolves resource" {
    checkAll(Arb.int()) { n ->
      val r = resource(Resource({ n }, { _, _ -> Unit }))

      (r + 1) shouldBe n + 1
    }
  }

  "value resource is released with Completed" {
    checkAll(Arb.int()) { n: Int ->
      val p = CompletableDeferred<ExitCase>()

      resource(Resource({ n }, { _, ex -> p.complete(ex) }))

      afterSpec {
        p.await().shouldBeCompleted()
      }
    }
  }

  val exitCase = CompletableDeferred<ExitCase>()
  val n = resource(Resource({ 24 }, { _, ex -> exitCase.complete(ex) }))

  "resource from SpecScope can be consumed and is released with Completed" {
    (n + 1) shouldBe 25
    afterSpec {
      exitCase.await().shouldBeCompleted()
    }
  }
})
