package io.kotest.assertions.arrow.fx.coroutines

import io.kotest.matchers.shouldBe as coreShouldBe
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope

internal infix fun <A> A.shouldBe(a: A): A {
  this coreShouldBe a
  return this
}

internal expect fun <T> runBlocking(
  context: CoroutineContext,
  block: suspend CoroutineScope.() -> T
): T
