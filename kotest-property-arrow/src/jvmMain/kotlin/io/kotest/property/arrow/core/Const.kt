package io.kotest.property.arrow.core

import arrow.core.Const
import io.kotest.property.Arb
import io.kotest.property.arbitrary.map

public fun <A, B> Arb.Companion.const(arb: Arb<A>): Arb<Const<A, B>> =
  arb.map { Const<A, B>(it) }
