package io.kotest.assertions.arrow.core

import arrow.core.NonEmptyList
import io.kotest.assertions.arrow.nel.containAll
import io.kotest.assertions.arrow.nel.haveDuplicates
import io.kotest.matchers.collections.shouldBeSorted as kShouldBeSorted
import io.kotest.matchers.collections.shouldNotBeSorted as kShouldNotBeSorted
import io.kotest.matchers.collections.shouldBeUnique
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainNoNulls
import io.kotest.matchers.collections.shouldContainNull
import io.kotest.matchers.collections.shouldContainOnlyNulls
import io.kotest.matchers.collections.shouldHaveElementAt
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.collections.shouldNotBeUnique
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.collections.shouldNotContainAll as kshouldNotContainAll
import io.kotest.matchers.collections.shouldNotContainNoNulls
import io.kotest.matchers.collections.shouldNotContainNull
import io.kotest.matchers.collections.shouldNotContainOnlyNulls
import io.kotest.matchers.collections.shouldNotHaveElementAt
import io.kotest.matchers.collections.shouldNotHaveSingleElement
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

fun <A> NonEmptyList<A>.shouldContainOnlyNulls(): NonEmptyList<A> =
  apply { all.shouldContainOnlyNulls() }

fun <A> NonEmptyList<A>.shouldNotContainOnlyNulls(): NonEmptyList<A> =
  apply { all.shouldNotContainOnlyNulls() }

fun <A> NonEmptyList<A>.shouldContainNull(): NonEmptyList<A> =
  apply { all.shouldContainNull() }

fun <A> NonEmptyList<A>.shouldNotContainNull(): NonEmptyList<A> =
  apply { all.shouldNotContainNull() }

fun <A> NonEmptyList<A>.shouldHaveElementAt(index: Int, element: A): Unit =
  all.shouldHaveElementAt(index, element)

fun <A> NonEmptyList<A>.shouldNotHaveElementAt(index: Int, element: A): Unit =
  all.shouldNotHaveElementAt(index, element)

fun <A> NonEmptyList<A>.shouldContainNoNulls(): NonEmptyList<A> =
  apply { all.shouldContainNoNulls() }

fun <A> NonEmptyList<A>.shouldNotContainNoNulls(): NonEmptyList<A> =
  apply { all.shouldNotContainNoNulls() }

infix fun <A> NonEmptyList<A>.shouldContain(a: A): Unit =
  all.shouldContain(a)

infix fun <A> NonEmptyList<A>.shouldNotContain(a: A): Unit =
  all.shouldNotContain(a)

fun <A> NonEmptyList<A>.shouldBeUnique(): NonEmptyList<A> =
  apply { all.shouldBeUnique() }

fun <A> NonEmptyList<A>.shouldNotBeUnique(): NonEmptyList<A> =
  apply { all.shouldNotBeUnique() }

fun <A> NonEmptyList<A>.shouldContainDuplicates(): NonEmptyList<A> =
  apply { should(haveDuplicates()) }

fun <A> NonEmptyList<A>.shouldNotContainDuplicates(): NonEmptyList<A> =
  apply { shouldNot(haveDuplicates()) }

fun <A> NonEmptyList<A>.shouldContainAll(vararg ts: A): Unit =
  should(containAll(*ts))

fun <A> NonEmptyList<A>.shouldNotContainAll(vararg ts: A): Unit =
  shouldNot(containAll(ts))

infix fun <A> NonEmptyList<A>.shouldContainAll(ts: List<A>): Unit =
  should(containAll(ts))

infix fun <A> NonEmptyList<A>.shouldNotContainAll(ts: List<A>): Unit =
  shouldNot(containAll(ts))

infix fun <A> NonEmptyList<A>.shouldHaveSize(size: Int): NonEmptyList<A> =
  apply { all.shouldHaveSize(size) }

infix fun <A> NonEmptyList<A>.shouldNotHaveSize(size: Int): NonEmptyList<A> =
  apply { all.shouldNotHaveSize(size) }

infix fun <A> NonEmptyList<A>.shouldHaveSingleElement(a: A): Unit =
  all.shouldHaveSingleElement(a)

infix fun <A> NonEmptyList<A>.shouldNotHaveSingleElement(a: A): Unit =
  all.shouldNotHaveSingleElement(a)

fun <A : Comparable<A>> NonEmptyList<A>.shouldBeSorted(): NonEmptyList<A> =
  apply { all.kShouldBeSorted() }

fun <A : Comparable<A>> NonEmptyList<A>.shouldNotBeSorted(): NonEmptyList<A> =
  apply { all.kShouldNotBeSorted() }
