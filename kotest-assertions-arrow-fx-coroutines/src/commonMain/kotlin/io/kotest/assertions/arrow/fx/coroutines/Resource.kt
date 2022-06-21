package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.Resource

public suspend fun <A> Resource<A>.shouldBeResource(a: A): A =
  use { it shouldBe a }

public suspend fun <A> Resource<A>.shouldBeResource(expected: Resource<A>): A =
  zip(expected).use { (a, b) -> a shouldBe b }
