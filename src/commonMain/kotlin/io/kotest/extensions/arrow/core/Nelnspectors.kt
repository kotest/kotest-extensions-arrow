package io.kotest.extensions.arrow.core

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

public fun <A> NonEmptyList<A>.forAll(f: (A) -> Unit): Unit =
  all.forAll(f)

public fun <A> NonEmptyList<A>.forOne(f: (A) -> Unit): Unit =
  all.forOne(f)

public fun <A> NonEmptyList<A>.forExactly(k: Int, f: (A) -> Unit): Unit =
  all.forExactly(k, f)

public fun <A> NonEmptyList<A>.forSome(f: (A) -> Unit): Unit =
  all.forSome(f)

public fun <A> NonEmptyList<A>.forAny(f: (A) -> Unit): Unit =
  all.forAny(f)

public fun <A> NonEmptyList<A>.forAtLeastOne(f: (A) -> Unit): Unit =
  all.forAtLeastOne(f)

public fun <A> NonEmptyList<A>.forAtLeast(k: Int, f: (A) -> Unit): Unit =
  all.forAtLeast(k, f)

public fun <A> NonEmptyList<A>.forAtMostOne(f: (A) -> Unit): Unit =
  all.forAtMostOne(f)

public fun <A> NonEmptyList<A>.forAtMost(k: Int, f: (A) -> Unit): Unit =
  all.forAtMost(k, f)

public fun <A> NonEmptyList<A>.forNone(f: (A) -> Unit): Unit =
  all.forNone(f)
