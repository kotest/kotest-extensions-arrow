package io.kotest.assertions.arrow.core

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * smart casts to [Validated.Valid] and fails with [failureMessage] otherwise
 */
@OptIn(ExperimentalContracts::class)
fun <E, A> Validated<E, A>.shouldBeValid(
  failureMessage: (Invalid<E>) -> String = { "Expected Validated.Valid, but found Invalid with value ${it.value}" }
): A {
  contract {
    returns() implies (this@shouldBeValid is Valid<A>)
  }
  return when (this) {
    is Valid -> value
    is Invalid -> throw AssertionError(failureMessage(this))
  }
}

/**
 * smart casts to [Validated.Invalid] and fails with [failureMessage] otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun <E, A> Validated<E, A>.shouldBeInvalid(
  failureMessage: (Valid<A>) -> String = { "Expected Validated.Invalid, but found Valid with value ${it.value}" }
): E {
  contract {
    returns() implies (this@shouldBeInvalid is Invalid<E>)
  }
  return when (this) {
    is Valid -> throw AssertionError(failureMessage(this))
    is Invalid -> value
  }
}
