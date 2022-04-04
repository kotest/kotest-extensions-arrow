package io.kotest.assertions.arrow.fx.coroutines

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

/** JVM `runBlocking` implementation */
public actual fun <T> runBlocking(
  context: CoroutineContext,
  block: suspend CoroutineScope.() -> T
): T = runBlocking(context, block)
