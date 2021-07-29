package io.kotest.assertions.arrow.core

import arrow.core.NonEmptyList
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forAny
import io.kotest.inspectors.forAtLeast
import io.kotest.inspectors.forAtLeastOne
import io.kotest.inspectors.forAtMost
import io.kotest.inspectors.forAtMostOne
import io.kotest.inspectors.forExactly
import io.kotest.inspectors.forNone
import io.kotest.inspectors.forOne
import io.kotest.inspectors.forSome
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

fun <A> NonEmptyList<A>.forAll(fn: (A) -> Unit): Unit =
  all.forAll(fn)

fun <A> NonEmptyList<A>.forOne(fn: (A) -> Unit): Unit =
  all.forOne(fn)

fun <A> NonEmptyList<A>.forExactly(k: Int, fn: (A) -> Unit): Unit =
  all.forExactly(k, fn)

fun <A> NonEmptyList<A>.forSome(fn: (A) -> Unit): Unit =
  all.forSome(fn)

fun <A> NonEmptyList<A>.forAny(f: (A) -> Unit): Unit =
  all.forAny(f)

fun <A> NonEmptyList<A>.forAtLeastOne(f: (A) -> Unit): Unit =
  all.forAtLeastOne(f)

fun <A> NonEmptyList<A>.forAtLeast(k: Int, fn: (A) -> Unit): Unit =
  all.forAtLeast(k, fn)

fun <A> NonEmptyList<A>.forAtMostOne(f: (A) -> Unit): Unit =
  all.forAtMostOne(f)

fun <A> NonEmptyList<A>.forAtMost(k: Int, fn: (A) -> Unit): Unit =
  all.forAtMost(k, fn)

fun <T> NonEmptyList<T>.forNone(f: (T) -> Unit): Unit =
  all.forNone(f)

fun <A> NonEmptyList<A>.shouldContainOnlyNulls(): Unit =
  should(containOnlyNulls())

fun <A> NonEmptyList<A>.shouldNotContainOnlyNulls(): Unit =
  shouldNot(containOnlyNulls())

fun <A> containOnlyNulls(): Matcher<NonEmptyList<A>> =
  object : Matcher<NonEmptyList<A>> {
    override fun test(value: NonEmptyList<A>): MatcherResult =
      MatcherResult(
        value.all.all { it == null },
        "NonEmptyList should contain only nulls",
        "NonEmptyList should not contain only nulls"
      )
  }

fun <A> NonEmptyList<A>.shouldContainNull(): Unit =
  should(containNull())

fun <A> NonEmptyList<A>.shouldNotContainNull(): Unit =
  shouldNot(containNull())

fun <A> containNull(): Matcher<NonEmptyList<A>> =
  object : Matcher<NonEmptyList<A>> {
    override fun test(value: NonEmptyList<A>) =
      MatcherResult(
        value.all.any { it == null },
        "NonEmptyList should contain at least one null",
        "NonEmptyList should not contain any nulls"
      )
  }

fun <A> NonEmptyList<A>.shouldContainElementAt(index: Int, element: A): Unit =
  should(haveElementAt(index, element))

fun <A> NonEmptyList<A>.shouldNotContainElementAt(index: Int, element: A): Unit =
  shouldNot(haveElementAt(index, element))

fun <A> haveElementAt(index: Int, element: A): Matcher<NonEmptyList<A>> =
  object : Matcher<NonEmptyList<A>> {
    override fun test(value: NonEmptyList<A>): MatcherResult =
      MatcherResult(
        value.all[index] == element,
        "NonEmptyList should contain $element at index $index",
        "NonEmptyList should not contain $element at index $index"
      )
  }

fun <A> NonEmptyList<A>.shouldContainNoNulls(): Unit =
  should(containNoNulls())

fun <A> NonEmptyList<A>.shouldNotContainNoNulls(): Unit =
  shouldNot(containNoNulls())

fun <A> containNoNulls(): Matcher<NonEmptyList<A>> =
  object : Matcher<NonEmptyList<A>> {
    override fun test(value: NonEmptyList<A>): MatcherResult =
      MatcherResult(
        value.all.all { it != null },
        "NonEmptyList should not contain nulls",
        "NonEmptyList should have at least one null"
      )
  }

infix fun <A> NonEmptyList<A>.shouldContain(a: A): Unit =
  should(contain(a))

infix fun <A> NonEmptyList<A>.shouldNotContain(a: A): Unit =
  shouldNot(contain(a))

fun <T> contain(a: T): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult =
      MatcherResult(
        value.all.contains(a),
        "NonEmptyList should contain element $a",
        "NonEmptyList should not contain element $a"
      )
  }

fun NonEmptyList<Any>.shouldBeUnique(): Unit =
  shouldNot(haveDuplicates())

fun NonEmptyList<Any>.shouldNotBeUnique(): Unit =
  should(haveDuplicates())

fun NonEmptyList<Any>.shouldHaveDuplicates(): Unit =
  should(haveDuplicates())

fun NonEmptyList<Any>.shouldNotHaveDuplicates(): Unit =
  shouldNot(haveDuplicates())

fun <T> haveDuplicates(): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult =
      MatcherResult(
        value.all.toSet().size < value.size,
        "NonEmptyList should contain duplicates",
        "NonEmptyList should not contain duplicates"
      )
  }

fun <T> NonEmptyList<T>.shouldContainAll(vararg ts: T): Unit =
  should(containAll(*ts))

fun <T> NonEmptyList<T>.shouldNotContainAll(vararg ts: T): Unit =
  shouldNot(containAll(*ts))

infix fun <T> NonEmptyList<T>.shouldContainAll(ts: List<T>): Unit =
  should(containAll(ts))

infix fun <T> NonEmptyList<T>.shouldNotContainAll(ts: List<T>): Unit =
  shouldNot(containAll(ts))

fun <T> containAll(vararg ts: T): Matcher<NonEmptyList<T>> =
  containAll(ts.asList())

fun <T> containAll(ts: List<T>): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult =
      MatcherResult(
        ts.all { value.contains(it) },
        "NonEmptyList should contain all of ${ts.joinToString(",", limit = 10)}",
        "NonEmptyList should not contain all of ${ts.joinToString(",", limit = 10)}"
      )
  }

infix fun NonEmptyList<Any>.shouldHaveSize(size: Int): Unit =
  should(haveSize(size))

infix fun NonEmptyList<Any>.shouldNotHaveSize(size: Int): Unit =
  shouldNot(haveSize(size))

fun <T> haveSize(size: Int): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult =
      MatcherResult(
        value.size == size,
        "NonEmptyList should have size $size but has size ${value.size}",
        "NonEmptyList should not have size $size"
      )
  }

infix fun <T> NonEmptyList<T>.shouldBeSingleElement(a: T): Unit =
  should(singleElement(a))

infix fun <T> NonEmptyList<T>.shouldNotBeSingleElement(a: T): Unit =
  shouldNot(singleElement(a))

fun <T> singleElement(a: T): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult = MatcherResult(
      value.size == 1 && value.head == a,
      "NonEmptyList should be a single element of $a but has ${value.size} elements",
      "NonEmptyList should not be a single element of $a"
    )
  }

fun <T : Comparable<T>> NonEmptyList<T>.shouldBeSorted(): Unit =
  should(beSorted())

fun <T : Comparable<T>> NonEmptyList<T>.shouldNotBeSorted(): Unit =
  shouldNot(beSorted())

fun <T : Comparable<T>> beSorted(): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult {
      val passed = value.all.sorted() == value.all
      val snippet = value.all.joinToString(",", limit = 10)
      return MatcherResult(
        passed,
        "NonEmptyList $snippet should be sorted",
        "NonEmptyList $snippet should not be sorted"
      )
    }
  }
