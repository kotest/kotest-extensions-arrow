package io.kotest.assertions.arrow.fx.coroutines

import io.kotest.matchers.shouldBe as coreShouldBe
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal infix fun <A> A.shouldBe(a: A): A {
  this coreShouldBe a
  return this
}

internal expect fun <T> runBlocking(
  context: CoroutineContext = Dispatchers.Unconfined,
  block: suspend CoroutineScope.() -> T
): T
