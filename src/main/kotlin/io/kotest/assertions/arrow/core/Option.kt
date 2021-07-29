package io.kotest.assertions.arrow.core

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * smart casts to [Some] and fails with [failureMessage] otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun <A> Option<A>.shouldBeSome(failureMessage: () -> String = { "Expected Some, but found None" }): A {
  contract {
    returns() implies (this@shouldBeSome is Some<A>)
  }
  return when (this) {
    None -> throw AssertionError(failureMessage())
    is Some -> value
  }
}

/**
 * smart casts to [None] and fails with [failureMessage] otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun <A> Option<A>.shouldBeNone(failureMessage: (Some<A>) -> String = { "Expected None, but found Some with value ${it.value}" }): None {
  contract {
    returns() implies (this@shouldBeNone is None)
  }
  return when (this) {
    None -> None
    is Some -> throw AssertionError(failureMessage(this))
  }
}
