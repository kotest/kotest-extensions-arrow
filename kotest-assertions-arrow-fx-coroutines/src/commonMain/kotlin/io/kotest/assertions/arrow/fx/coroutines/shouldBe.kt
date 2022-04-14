package io.kotest.assertions.arrow.fx.coroutines

import io.kotest.matchers.shouldBe as coreShouldBe
import kotlin.jvm.JvmName

internal infix fun <A> A.shouldBe(a: A): A {
  this coreShouldBe a
  return this
}
