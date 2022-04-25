package io.kotest.assertions.arrow.fx.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.await

@Suppress("ILLEGAL_SUSPEND_FUNCTION_CALL")
internal actual fun <T> runBlocking(
  context: CoroutineContext,
  block: suspend CoroutineScope.() -> T
): T =
  GlobalScope.promise(context) { block() }.await()
