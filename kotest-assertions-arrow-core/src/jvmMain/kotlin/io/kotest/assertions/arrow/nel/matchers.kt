package io.kotest.assertions.arrow.nel

import arrow.core.NonEmptyList
import io.kotest.assertions.arrow.core.shouldBeSorted
import io.kotest.assertions.arrow.core.shouldBeUnique
import io.kotest.assertions.arrow.core.shouldContain
import io.kotest.assertions.arrow.core.shouldContainAll
import io.kotest.assertions.arrow.core.shouldContainDuplicates
import io.kotest.assertions.arrow.core.shouldContainNoNulls
import io.kotest.assertions.arrow.core.shouldContainNull
import io.kotest.assertions.arrow.core.shouldContainOnlyNulls
import io.kotest.assertions.arrow.core.shouldHaveElementAt
import io.kotest.assertions.arrow.core.shouldHaveSingleElement
import io.kotest.assertions.arrow.core.shouldHaveSize
import io.kotest.assertions.arrow.core.shouldNotBeSorted
import io.kotest.assertions.arrow.core.shouldNotBeUnique
import io.kotest.assertions.arrow.core.shouldNotContain
import io.kotest.assertions.arrow.core.shouldNotContainAll
import io.kotest.assertions.arrow.core.shouldNotContainDuplicates
import io.kotest.assertions.arrow.core.shouldNotContainNoNulls
import io.kotest.assertions.arrow.core.shouldNotContainNull
import io.kotest.assertions.arrow.core.shouldNotContainOnlyNulls
import io.kotest.assertions.arrow.core.shouldNotHaveElementAt
import io.kotest.assertions.arrow.core.shouldNotHaveSingleElement
import io.kotest.assertions.arrow.core.shouldNotHaveSize
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult

@Deprecated(
  "Use shouldContainOnlyNulls from core",
  ReplaceWith("shouldContainOnlyNulls()", "io.kotest.assertions.arrow.core.shouldContainOnlyNulls")
)
public fun <A> NonEmptyList<A>.shouldContainOnlyNulls(): Unit {
  shouldContainOnlyNulls()
}

@Deprecated(
  "Use shouldNotContainOnlyNulls from core",
  ReplaceWith("shouldNotContainOnlyNulls()", "io.kotest.assertions.arrow.core.shouldNotContainOnlyNulls")
)
public fun <A> NonEmptyList<A>.shouldNotContainOnlyNulls(): Unit {
  shouldNotContainOnlyNulls()
}

@Deprecated("Use shouldNotContainOnlyNulls, shouldContainOnlyNulls directly")
public fun <A> containOnlyNulls(): Matcher<NonEmptyList<A>> =
  object : Matcher<NonEmptyList<A>> {
    override fun test(value: NonEmptyList<A>): MatcherResult =
      MatcherResult(
        value.all.all { it == null },
        "NonEmptyList should contain only nulls",
        "NonEmptyList should not contain only nulls"
      )
  }

@Deprecated(
  "Use shouldContainNull from core",
  ReplaceWith("shouldContainNull()", "io.kotest.assertions.arrow.core.shouldContainNull")
)
public fun <A> NonEmptyList<A>.shouldContainNull(): Unit {
  shouldContainNull()
}

@Deprecated(
  "Use shouldNotContainNull from core",
  ReplaceWith("shouldNotContainNull()", "io.kotest.assertions.arrow.core.shouldNotContainNull")
)
public fun <A> NonEmptyList<A>.shouldNotContainNull(): Unit {
  shouldNotContainNull()
}


@Deprecated("Use shouldContainNull, shouldNotContainNull directly")
public fun <A> containNull(): Matcher<NonEmptyList<A>> =
  object : Matcher<NonEmptyList<A>> {
    override fun test(value: NonEmptyList<A>) =
      MatcherResult(
        value.all.any { it == null },
        "NonEmptyList should contain at least one null",
        "NonEmptyList should not contain any nulls"
      )
  }

@Deprecated(
  "Use shouldHaveElementAt",
  ReplaceWith("shouldHaveElementAt(index, element)", "io.kotest.assertions.arrow.core.shouldHaveElementAt")
)
public fun <A> NonEmptyList<A>.shouldContainElementAt(index: Int, element: A): Unit =
  shouldHaveElementAt(index, element)

@Deprecated(
  "Use shouldNotHaveElementAt from core",
  ReplaceWith("shouldNotHaveElementAt(index, element)", "io.kotest.assertions.arrow.core.shouldNotHaveElementAt")
)
public fun <A> NonEmptyList<A>.shouldNotContainElementAt(index: Int, element: A): Unit =
  shouldNotHaveElementAt(index, element)

@Deprecated("Use shouldContainElementAt or shouldNotContainElementAt directly")
public fun <A> haveElementAt(index: Int, element: A): Matcher<NonEmptyList<A>> =
  object : Matcher<NonEmptyList<A>> {
    override fun test(value: NonEmptyList<A>): MatcherResult =
      MatcherResult(
        value.all[index] == element,
        "NonEmptyList should contain $element at index $index",
        "NonEmptyList should not contain $element at index $index"
      )
  }

@Deprecated(
  "Use shouldContainNoNulls from core",
  ReplaceWith("shouldContainNoNulls()", "io.kotest.assertions.arrow.core.shouldContainNoNulls")
)
public fun <A> NonEmptyList<A>.shouldContainNoNulls(): Unit {
  shouldContainNoNulls()
}

@Deprecated(
  "Use shouldNotContainNoNulls from core",
  ReplaceWith("shouldNotContainNoNulls()", "io.kotest.assertions.arrow.core.shouldNotContainNoNulls")
)
public fun <A> NonEmptyList<A>.shouldNotContainNoNulls(): Unit {
  shouldNotContainNoNulls()
}

@Deprecated("Use shouldNotContainNoNulls, shouldContainNoNulls directly")
public fun <A> containNoNulls(): Matcher<NonEmptyList<A>> =
  object : Matcher<NonEmptyList<A>> {
    override fun test(value: NonEmptyList<A>): MatcherResult =
      MatcherResult(
        value.all.all { it != null },
        "NonEmptyList should not contain nulls",
        "NonEmptyList should have at least one null"
      )
  }

@Deprecated(
  "Use shouldContain from core",
  ReplaceWith("shouldContain(a)", "io.kotest.assertions.arrow.core.shouldContain")
)
public infix fun <A> NonEmptyList<A>.shouldContain(a: A): Unit =
  shouldContain(a)

@Deprecated(
  "Use shouldNotContain from core",
  ReplaceWith("shouldNotContain(a)", "io.kotest.assertions.arrow.core.shouldNotContain")
)
public infix fun <A> NonEmptyList<A>.shouldNotContain(a: A): Unit =
  shouldNotContain(a)

@Deprecated("Use shouldContain, shouldContainNoNulls directly")
public fun <T> contain(a: T): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult =
      MatcherResult(
        value.all.contains(a),
        { "NonEmptyList should contain element $a" },
        {
          "NonEmptyList should not contain element $a"
        }
      )
  }

@Deprecated(
  "Use shouldBeUnique from core",
  ReplaceWith("shouldBeUnique()", "io.kotest.assertions.arrow.core.shouldBeUnique")
)
public fun NonEmptyList<Any>.shouldBeUnique(): Unit {
  shouldBeUnique()
}

@Deprecated(
  "Use shouldNotBeUnique from core",
  ReplaceWith("shouldNotBeUnique()", "io.kotest.assertions.arrow.core.shouldNotBeUnique")
)
public fun NonEmptyList<Any>.shouldNotBeUnique(): Unit {
  shouldNotBeUnique()
}

@Deprecated(
  "Use shouldContainDuplicates",
  ReplaceWith("shouldContainDuplicates()", "io.kotest.assertions.arrow.core.shouldContainDuplicates")
)
public fun NonEmptyList<Any>.shouldHaveDuplicates(): Unit {
  shouldContainDuplicates()
}

@Deprecated(
  "Use shouldNotContainDuplicates",
  ReplaceWith("shouldNotContainDuplicates()", "io.kotest.assertions.arrow.core.shouldNotContainDuplicates")
)
public fun NonEmptyList<Any>.shouldNotHaveDuplicates(): Unit {
  shouldNotContainDuplicates()
}

@Deprecated("Use shouldBeUnique, shouldNotBeUnique, shouldHaveDuplicates, shouldNotHaveDuplicates directly")
public fun <T> haveDuplicates(): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult =
      MatcherResult(
        value.all.toSet().size < value.size,
        { "NonEmptyList should contain duplicates" },
        {
          "NonEmptyList should not contain duplicates"
        }
      )
  }

@Deprecated(
  "Use shouldContainAll",
  ReplaceWith("shouldContainAll(ts)", "io.kotest.assertions.arrow.core.shouldContainAll")
)
public fun <T> NonEmptyList<T>.shouldContainAll(vararg ts: T): Unit =
  shouldContainAll(*ts)

@Deprecated(
  "Use shouldNotContainAll",
  ReplaceWith("shouldNotContainAll(ts)", "io.kotest.assertions.arrow.core.shouldNotContainAll")
)
public fun <T> NonEmptyList<T>.shouldNotContainAll(vararg ts: T): Unit =
  shouldNotContainAll(*ts)

@Deprecated(
  "Use shouldContainAll",
  ReplaceWith("shouldContainAll(ts)", "io.kotest.assertions.arrow.core.shouldContainAll")
)
public infix fun <T> NonEmptyList<T>.shouldContainAll(ts: List<T>): Unit =
  shouldContainAll(ts)

@Deprecated(
  "Use shouldNotContainAll",
  ReplaceWith("shouldNotContainAll(ts)", "io.kotest.assertions.arrow.core.shouldNotContainAll")
)
public infix fun <T> NonEmptyList<T>.shouldNotContainAll(ts: List<T>): Unit =
  shouldNotContainAll(ts)

@Deprecated("Use shouldContainAll, shouldNotContainAll, shouldContainAll, shouldNotContainAll directly")
public fun <T> containAll(vararg ts: T): Matcher<NonEmptyList<T>> =
  containAll(ts.toList())

@Deprecated("Use shouldContainAll, shouldNotContainAll, shouldContainAll, shouldNotContainAll directly")
public fun <T> containAll(ts: List<T>): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult =
      MatcherResult(
        ts.all { value.contains(it) },
        { "NonEmptyList should contain all of ${ts.joinToString(",", limit = 10)}" },
        {
          "NonEmptyList should not contain all of ${ts.joinToString(",", limit = 10)}"
        }
      )
  }

@Deprecated(
  "Use shouldHaveSize",
  ReplaceWith("shouldHaveSize(size)", "io.kotest.assertions.arrow.core.shouldHaveSize")
)
public infix fun NonEmptyList<Any>.shouldHaveSize(size: Int): Unit {
  shouldHaveSize(size)
}

@Deprecated(
  "Use shouldNotHaveSize",
  ReplaceWith("shouldNotHaveSize(size)", "io.kotest.assertions.arrow.core.shouldNotHaveSize")
)
public infix fun NonEmptyList<Any>.shouldNotHaveSize(size: Int): Unit {
  shouldNotHaveSize(size)
}

@Deprecated("Use shouldHaveSize, shouldNotHaveSize directly")
public fun <T> haveSize(size: Int): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult =
      MatcherResult(
        value.size == size,
        { "NonEmptyList should have size $size but has size ${value.size}" },
        {
          "NonEmptyList should not have size $size"
        }
      )
  }

@Deprecated(
  "Use shouldHaveSingleElement",
  ReplaceWith("shouldHaveSingleElement(a)", "io.kotest.assertions.arrow.core.shouldHaveSingleElement")
)
public infix fun <T> NonEmptyList<T>.shouldBeSingleElement(a: T): Unit =
  shouldHaveSingleElement(a)

@Deprecated(
  "Use shouldNotHaveSingleElement",
  ReplaceWith("shouldNotHaveSingleElement(a)", "io.kotest.assertions.arrow.core.shouldNotHaveSingleElement")
)
public infix fun <T> NonEmptyList<T>.shouldNotBeSingleElement(a: T): Unit =
  shouldNotHaveSingleElement(a)

@Deprecated("Use shouldBeSingleElement, shouldNotBeSingleElement directly")
public fun <T> singleElement(a: T): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult = MatcherResult(
      value.size == 1 && value.head == a,
      { "NonEmptyList should be a single element of $a but has ${value.size} elements" },
      {
        "NonEmptyList should not be a single element of $a"
      }
    )
  }

@Deprecated(
  "Use shouldBeSorted",
  ReplaceWith("shouldBeSorted()", "io.kotest.assertions.arrow.core.shouldBeSorted")
)
public fun <T : Comparable<T>> NonEmptyList<T>.shouldBeSorted(): Unit {
  shouldBeSorted()
}

@Deprecated(
  "Use shouldNotBeSorted",
  ReplaceWith("shouldNotBeSorted()", "io.kotest.assertions.arrow.core.shouldNotBeSorted")
)
public fun <T : Comparable<T>> NonEmptyList<T>.shouldNotBeSorted(): Unit =
  shouldNotBeSorted().run { }

@Deprecated("Use shouldBeSorted or shouldNotBeSorted directly")
public fun <T : Comparable<T>> beSorted(): Matcher<NonEmptyList<T>> =
  object : Matcher<NonEmptyList<T>> {
    override fun test(value: NonEmptyList<T>): MatcherResult {
      val passed = value.all.sorted() == value.all
      val snippet = value.all.joinToString(",", limit = 10)
      return MatcherResult(
        passed,
        { "NonEmptyList $snippet should be sorted" },
        {
          "NonEmptyList $snippet should not be sorted"
        }
      )
    }
  }
