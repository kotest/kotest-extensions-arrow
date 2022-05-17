package io.kotest.assertions.arrow.fx.coroutines

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.completeWith
import kotlin.coroutines.intrinsics.startCoroutineUninterceptedOrReturn

class UtilitiesSpec : StringSpec({
  "suspended always suspends" {
    checkAll(Arb.int()) { i ->
      val promise = CompletableDeferred<Int>()

      val x = i.suspended()
        .startCoroutineUninterceptedOrReturn(
          Continuation(EmptyCoroutineContext) {
            promise.completeWith(it)
          }
        )

      x shouldBe COROUTINE_SUSPENDED
      promise.await() shouldBe i
    }
  }
})
