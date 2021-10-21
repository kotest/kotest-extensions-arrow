package io.kotest.assertions.arrow.nel

import arrow.core.NonEmptyList
import io.kotest.assertions.arrow.core.forAll
import io.kotest.assertions.arrow.core.forExactly
import io.kotest.assertions.arrow.core.forOne
import io.kotest.assertions.arrow.core.forSome
import io.kotest.assertions.arrow.core.forAny
import io.kotest.assertions.arrow.core.forAtLeastOne
import io.kotest.assertions.arrow.core.forAtLeast
import io.kotest.assertions.arrow.core.forAtMostOne
import io.kotest.assertions.arrow.core.forAtMost
import io.kotest.assertions.arrow.core.forNone

@Deprecated("Use forAll from core", ReplaceWith("forAll(fn)", "io.kotest.assertions.arrow.core.forAll"))
public fun <T> NonEmptyList<T>.forAll(fn: (T) -> Unit): Unit = forAll(fn)

@Deprecated("Use forOne from core", ReplaceWith("forOne(fn)", "io.kotest.assertions.arrow.core.forOne"))
public fun <T> NonEmptyList<T>.forOne(fn: (T) -> Unit): Unit = forOne(fn)

@Deprecated("Use forExactly from core", ReplaceWith("forExactly(k, fn)", "io.kotest.assertions.arrow.core.forExactly"))
public fun <T> NonEmptyList<T>.forExactly(k: Int, fn: (T) -> Unit): Unit = forExactly(k, fn)

@Deprecated("Use forSome from core", ReplaceWith("forExactly(fn)", "io.kotest.assertions.arrow.core.forExactly"))
public fun <T> NonEmptyList<T>.forSome(fn: (T) -> Unit): Unit = forSome(fn)

@Deprecated("Use forAny from core", ReplaceWith("forAny(f)", "io.kotest.assertions.arrow.core.forAny"))
public fun <T> NonEmptyList<T>.forAny(f: (T) -> Unit): Unit = forAny(f)

@Deprecated(
  "Use forAtLeastOne from core",
  ReplaceWith("forAtLeastOne(f)", "io.kotest.assertions.arrow.core.forAtLeastOne")
)
public fun <T> NonEmptyList<T>.forAtLeastOne(f: (T) -> Unit): Unit = forAtLeastOne(f)

@Deprecated(
  "Use forAtLeast from core",
  ReplaceWith("forAtLeast(k, fn)", "io.kotest.assertions.arrow.core.forAtLeast")
)
public fun <T> NonEmptyList<T>.forAtLeast(k: Int, fn: (T) -> Unit): Unit = forAtLeast(k, fn)


@Deprecated(
  "Use forAtMostOne from core",
  ReplaceWith("forAtMostOne(f)", "io.kotest.assertions.arrow.core.forAtMostOne")
)
public fun <T> NonEmptyList<T>.forAtMostOne(f: (T) -> Unit): Unit = forAtMostOne(f)

@Deprecated(
  "Use forAtMost from core",
  ReplaceWith("forAtMost(k, fn)", "io.kotest.assertions.arrow.core.forAtMost")
)
public fun <T> NonEmptyList<T>.forAtMost(k: Int, fn: (T) -> Unit): Unit = forAtMost(k, fn)


@Deprecated(
  "Use forNone from core",
  ReplaceWith("forNone(f)", "io.kotest.assertions.arrow.core.forNone")
)
public fun <T> NonEmptyList<T>.forNone(f: (T) -> Unit): Unit = forNone(f)
