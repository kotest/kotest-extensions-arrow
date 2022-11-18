package io.kotest.assertions.arrow.fx.coroutines

import arrow.core.identity
import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource

public suspend infix fun <A> Resource<A>.shouldBeResource(a: A): A =
  use { it shouldBe a }

public suspend infix fun <A> Resource<A>.shouldBeResource(expected: Resource<A>): A =
  resource {
    bind() shouldBe expected.bind()
  }.use(::identity)
