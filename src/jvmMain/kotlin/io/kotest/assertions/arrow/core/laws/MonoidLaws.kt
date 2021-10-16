@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package io.kotest.assertions.arrow.core.laws

import arrow.typeclasses.Monoid
import io.kotest.assertions.arrow.laws.Law
import io.kotest.assertions.arrow.laws.equalUnderTheLaw
import io.kotest.assertions.arrow.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropertyContext
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

public object MonoidLaws {

  public fun <F> laws(M: Monoid<F>, GEN: Arb<F>, eq: (F, F) -> Boolean = { a, b -> a == b }): List<Law> =
    SemigroupLaws.laws(M, GEN, eq) +
      listOf(
        Law("Monoid Laws: Left identity") { M.monoidLeftIdentity(GEN, eq) },
        Law("Monoid Laws: Right identity") { M.monoidRightIdentity(GEN, eq) },
        Law("Monoid Laws: combineAll should be derived") { M.combineAllIsDerived(GEN, eq) },
        Law("Monoid Laws: combineAll of empty list is empty") { M.combineAllOfEmptyIsEmpty(eq) }
      )

  public suspend fun <F> Monoid<F>.monoidLeftIdentity(GEN: Arb<F>, eq: (F, F) -> Boolean): PropertyContext =
    checkAll(GEN) { a ->
      (empty().combine(a)).equalUnderTheLaw(a, eq)
    }

  public suspend fun <F> Monoid<F>.monoidRightIdentity(GEN: Arb<F>, eq: (F, F) -> Boolean): PropertyContext =
    checkAll(GEN) { a ->
      a.combine(empty()).equalUnderTheLaw(a, eq)
    }

  public suspend fun <F> Monoid<F>.combineAllIsDerived(GEN: Arb<F>, eq: (F, F) -> Boolean): PropertyContext =
    checkAll(5, Arb.list(GEN)) { list ->
      list.combineAll().equalUnderTheLaw(if (list.isEmpty()) empty() else list.reduce { acc, f -> acc.combine(f) }, eq)
    }

  public fun <F> Monoid<F>.combineAllOfEmptyIsEmpty(eq: (F, F) -> Boolean): Boolean=
    emptyList<F>().combineAll().equalUnderTheLaw(empty(), eq) shouldBe true
}
