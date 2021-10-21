package io.kotest.property.arrow.core

import arrow.core.Validated
import arrow.core.Validated.Invalid
import arrow.core.Validated.Valid
import arrow.core.ValidatedNel
import arrow.core.invalidNel
import arrow.core.validNel
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.map

public fun <E, A> Arb.Companion.validated(invalid: Arb<E>, valid: Arb<A>): Arb<Validated<E, A>> =
  choice(invalid.map(::Invalid), valid.map(::Valid))

public fun <E, A> Arb.Companion.validatedNel(invalidNel: Arb<E>, valid: Arb<A>): Arb<ValidatedNel<E, A>> =
  choice(invalidNel.map { it.invalidNel() }, valid.map { r -> r.validNel() })
