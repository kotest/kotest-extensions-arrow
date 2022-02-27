@file:OptIn(ExperimentalContracts::class)

package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.ExitCase
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

public fun ExitCase.shouldBeCancelled(
  failureMessage: (ExitCase) -> String = { "Expected ExitCase.Cancelled, but found $it" }
): ExitCase.Cancelled {
  contract {
    returns() implies (this@shouldBeCancelled is ExitCase.Cancelled)
  }
  return when (this) {
    is ExitCase.Completed -> throw AssertionError(failureMessage(this))
    is ExitCase.Cancelled -> this
    is ExitCase.Failure -> throw AssertionError(failureMessage(this))
  }
}

public fun ExitCase.shouldBeCompleted(
  failureMessage: (ExitCase) -> String = { "Expected ExitCase.Completed, but found $it" }
): ExitCase.Completed {
  contract {
    returns() implies (this@shouldBeCompleted is ExitCase.Completed)
  }
  return when (this) {
    is ExitCase.Completed -> this
    is ExitCase.Cancelled -> throw AssertionError(failureMessage(this))
    is ExitCase.Failure -> throw AssertionError(failureMessage(this))
  }
}

public fun ExitCase.shouldBeFailure(
  failureMessage: (ExitCase) -> String = { "Expected ExitCase.Failure, but found $it" }
): ExitCase.Failure {
  contract {
    returns() implies (this@shouldBeFailure is ExitCase.Failure)
  }
  return when (this) {
    is ExitCase.Completed -> throw AssertionError(failureMessage(this))
    is ExitCase.Cancelled -> throw AssertionError(failureMessage(this))
    is ExitCase.Failure -> this
  }
}
