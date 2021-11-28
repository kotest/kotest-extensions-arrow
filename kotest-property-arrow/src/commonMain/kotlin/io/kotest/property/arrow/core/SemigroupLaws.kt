@file:Suppress("MemberVisibilityCanBePrivate")

package io.kotest.property.arrow.core

import arrow.typeclasses.Semigroup
import io.kotest.property.Arb
import io.kotest.property.PropertyContext
import io.kotest.property.arrow.laws.Law
import io.kotest.property.arrow.laws.equalUnderTheLaw
import io.kotest.property.checkAll

public object SemigroupLaws {

  public fun <F> laws(SG: Semigroup<F>, G: Arb<F>, eq: (F, F) -> Boolean = { a, b -> a == b }): List<Law> =
    listOf(Law("Semigroup: associativity") { SG.semigroupAssociative(G, eq) })

  public suspend fun <F> Semigroup<F>.semigroupAssociative(G: Arb<F>, eq: (F, F) -> Boolean): PropertyContext =
    checkAll(G, G, G) { A, B, C ->
      A.combine(B).combine(C).equalUnderTheLaw(A.combine(B.combine(C)), eq)
    }
}
