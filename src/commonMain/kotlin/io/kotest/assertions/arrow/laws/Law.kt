package io.kotest.assertions.arrow.laws

import io.kotest.assertions.fail
import io.kotest.core.spec.style.scopes.RootContext
import io.kotest.core.test.TestContext
import io.kotest.core.test.createTestName

public data class Law(val name: String, val test: suspend TestContext.() -> Unit)

public fun <A> A.equalUnderTheLaw(b: A, f: (A, A) -> Boolean = { a, b -> a == b }): Boolean =
  if (f(this, b)) true else fail("Found $this but expected: $b")

public fun RootContext.testLaws(vararg laws: List<Law>): Unit =
  laws
    .flatMap { list: List<Law> -> list.asIterable() }
    .distinctBy { law: Law -> law.name }
    .forEach { law ->
      registration().addTest(createTestName(law.name), xdisabled = false, law.test)
    }

public fun RootContext.testLaws(prefix: String, vararg laws: List<Law>): Unit =
  laws
    .flatMap { list: List<Law> -> list.asIterable() }
    .distinctBy { law: Law -> law.name }
    .forEach { law: Law ->
      registration().addTest(createTestName(prefix, law.name, true), xdisabled = false, law.test)
    }
