package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.Schedule

/**
 * Recurs the effect [n] times, and collects the output along the way.
 */
public fun <A> recurAndCollect(n: Int): Schedule<A, List<A>> =
  Schedule.recurs<A>(n).zipRight(Schedule.identity<A>().collect())
