package io.kotest.assertions.arrow.fx.coroutines

import io.kotest.matchers.shouldBe as coreShouldBe

internal infix fun <A> A.shouldBe(a: A): A {
  this coreShouldBe a
  return this
}
