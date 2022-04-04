package io.kotest.assertions.arrow.fx.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

internal actual fun <T> runBlocking(
  context: CoroutineContext,
  block: suspend CoroutineScope.() -> T
): T =
  kotlinx.coroutines.runBlocking(context, block)
