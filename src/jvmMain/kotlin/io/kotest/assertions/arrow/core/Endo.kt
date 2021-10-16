package io.kotest.assertions.arrow.core

import arrow.core.Endo
import io.kotest.property.Arb
import io.kotest.property.arbitrary.map

public fun <A> Arb.Companion.endo(arb: Arb<A>): Arb<Endo<A>> =
  arb.map { a: A -> Endo { a } }
