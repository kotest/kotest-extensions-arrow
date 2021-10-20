package io.kotest.property.arrow.core

import arrow.core.NonEmptyList
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map

public fun <A> Arb.Companion.nonEmptyList(a: Arb<A>): Arb<NonEmptyList<A>> =
  list(a).filter(List<A>::isNotEmpty).map(NonEmptyList.Companion::fromListUnsafe)
