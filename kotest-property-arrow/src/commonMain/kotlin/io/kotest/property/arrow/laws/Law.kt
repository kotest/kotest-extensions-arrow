package io.kotest.property.arrow.laws

import io.kotest.assertions.fail
import io.kotest.core.spec.DslDrivenSpec
import io.kotest.core.spec.style.funSpec
import io.kotest.core.test.TestScope

public data class Law(val name: String, val test: suspend TestScope.() -> Unit)

public fun <A> A.equalUnderTheLaw(other: A, f: (A, A) -> Boolean = { a, b -> a == b }): Boolean =
  if (f(this, other)) true else fail("Found $this but expected: $other")

public fun DslDrivenSpec.testLaws(vararg laws: List<Law>): Unit =
  include(funSpec {
    laws
      .flatMap { list: List<Law> -> list.asIterable() }
      .distinctBy { law: Law -> law.name }
      .forEach { law ->
        test(law.name) { law.test(this) }
      }
  })

public fun DslDrivenSpec.testLaws(prefix: String, vararg laws: List<Law>): Unit =
  include(funSpec {
    laws
      .flatMap { list: List<Law> -> list.asIterable() }
      .distinctBy { law: Law -> law.name }
      .forEach { law: Law ->
        test("$prefix ${law.name}") { law.test(this) }
      }
  })
