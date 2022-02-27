package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.guaranteeCase
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.intercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.startCoroutine
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.Channel

/**
 * awaits the exitCase of [channel] and completes [exit] with it
 */
public suspend inline fun <A> awaitExitCase(
  channel: Channel<A>,
  exit: CompletableDeferred<ExitCase>
): A =
  guaranteeCase({
    channel.receive()
    awaitCancellation()
  }) { ex -> exit.complete(ex) }

/**
 * awaits the exitCase of [completableDeferred] and completes [exit] with it
 */
public suspend inline fun awaitExitCase(
  completableDeferred: CompletableDeferred<Unit>,
  exit: CompletableDeferred<ExitCase>
): Unit =
  awaitExitCase(completableDeferred, exit, Unit)

/**
 * awaits the exitCase of [completableDeferred] by passing [completeWith] and completes [exit] with it
 */
public suspend inline fun <A> awaitExitCase(
  completableDeferred: CompletableDeferred<A>,
  exit: CompletableDeferred<ExitCase>,
  completeWith: A
): A =
  guaranteeCase({
    completableDeferred.complete(completeWith)
    awaitCancellation()
  }) { ex -> exit.complete(ex) }

/**
 * turns a pure value into a suspended one, running in the [dispatcher]
 */
public suspend fun <A> A.suspend(dispatcher: CoroutineDispatcher = Dispatchers.Default): A =
  suspendCoroutineUninterceptedOrReturn { cont ->
    suspend { this }.startCoroutine(
      Continuation(dispatcher) {
        cont.intercepted().resumeWith(it)
      }
    )

    COROUTINE_SUSPENDED
  }

/**
 * throws the receiver in a suspended environment, running in [dispatcher]
 */
public suspend fun Throwable.suspend(dispatcher: CoroutineDispatcher = Dispatchers.Default): Nothing =
  suspendCoroutineUninterceptedOrReturn { cont ->
    suspend { throw this }.startCoroutine(
      Continuation(dispatcher) {
        cont.intercepted().resumeWith(it)
      }
    )

    COROUTINE_SUSPENDED
  }
