package io.kotest.assertions.arrow

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult

internal fun <A> matcher(
  passed: Boolean,
  msg: String,
  negatedFailureMsg: String = msg
): Matcher<A> =
  object : Matcher<A> {
    override fun test(value: A): MatcherResult = MatcherResult(passed, msg, negatedFailureMsg)
  }
