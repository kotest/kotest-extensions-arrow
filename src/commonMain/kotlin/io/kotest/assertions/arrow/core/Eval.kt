package io.kotest.assertions.arrow.core

import arrow.core.Eval
import io.kotest.property.Arb
import io.kotest.property.arbitrary.map

public fun <A> Arb<A>.evalNow(): Arb<Eval<A>> =
  map { Eval.now(it) }
