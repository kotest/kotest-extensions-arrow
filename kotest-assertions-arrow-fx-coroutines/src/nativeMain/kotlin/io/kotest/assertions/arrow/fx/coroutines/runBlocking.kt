package io.kotest.assertions.arrow.fx.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.runBlocking

internal actual fun <T> runBlocking(
  context: CoroutineContext,
  block: suspend CoroutineScope.() -> T
): T =
  runBlocking(context, block)
