package io.kotest.assertions.arrow.fx.coroutines

import arrow.core.Either
import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.bracketCase
import arrow.fx.coroutines.guaranteeCase
import arrow.fx.coroutines.never
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel

class ExitCaseTest : StringSpec({
  "ExitCase.Cancelled" {
    checkAll(Arb.int()) { a ->
      val s = Channel<Unit>()
      val pa = CompletableDeferred<Pair<Int, ExitCase>>()

      val f = async {
        guaranteeCase({ s.receive(); never<Int>() }) { ex -> pa.complete(Pair(a, ex)) }
      }

      s.send(Unit)
      f.cancel()

      pa.await().let { (res, exit) ->
        res shouldBe a
        exit.shouldBeCancelled()
      }
    }
  }

  "ExitCase.Completed" {
    checkAll(Arb.int()) { x ->
      val promise = CompletableDeferred<Pair<Int, ExitCase>>()

      Either.catch {
        bracketCase(
          acquire = { x },
          use = { it },
          release = { xx, ex ->
            require(promise.complete(Pair(xx, ex))) { "Release should only be called once, called again with $ex" }
              .suspend()
          }
        )
      }

      promise.await().second.shouldBeCompleted()
    }
  }

  "ExitCase.Failure" {
    checkAll(Arb.int(), Arb.string().map(::RuntimeException)) { x, e ->
      val promise = CompletableDeferred<Pair<Int, ExitCase>>()

      Either.catch {
        bracketCase<Int, Int>(
          acquire = { x },
          use = { e.suspend() },
          release = { xx, ex ->
            require(promise.complete(Pair(xx, ex))) { "Release should only be called once, called again with $ex" }
          }
        )
      }

      promise.await().second.shouldBeFailure(e)
    }
  }
})
