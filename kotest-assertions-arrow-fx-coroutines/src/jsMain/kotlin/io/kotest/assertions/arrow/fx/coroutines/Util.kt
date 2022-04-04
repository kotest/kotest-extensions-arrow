package io.kotest.assertions.arrow.fx.coroutines

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

/** JS `runBlocking` implementation */
public actual fun <T> runBlocking(
  context: CoroutineContext,
  block: suspend CoroutineScope.() -> T
): dynamic = GlobalScope.promise(context) { block() }
