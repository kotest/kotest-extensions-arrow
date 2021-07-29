package io.kotest.assertions.arrow.nel

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
