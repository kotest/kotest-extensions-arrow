package io.kotest.assertions.arrow.core

import arrow.core.Either
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.types.beInstanceOf2
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * smart casts Either to success case Either.Right<B> and fails with [failureMessage] in the failure case.
 * ```kotlin
 * import arrow.core.Either.Right
 * import arrow.core.Either
 * import arrow.core.shouldBeRight
 *
 * fun main() {
 *   //sampleStart
 *   fun squared(i: Int): Int = i * i
 *   val result = squared(5)
 *   val either = Either.conditionally(result == 25, { result }, { 25 })
 *   val a = either.shouldBeRight { "5 * 5 == 25 but was $it " }
 *   val smartCasted: Right<Int> = either
 *   //sampleEnd
 *   println(smartCasted)
 * }
 * ```
 */
@OptIn(ExperimentalContracts::class)
fun <A, B> Either<A, B>.shouldBeRight(failureMessage: (A) -> String = { "Expected Either.Right, but found Either.Left with value $it" }): B {
   contract {
      returns() implies (this@shouldBeRight is Either.Right<B>)
   }
   return when (this) {
      is Either.Right -> value
      is Either.Left -> throw AssertionError(failureMessage(value))
   }
}

/**
 * smart casts Either to failure case Either.Left<A> and fails with [failureMessage] in the success case.
 * ```kotlin
 * import arrow.core.Either.Left
 * import arrow.core.Either
 * import arrow.core.shouldBeLeft
 *
 * fun main() {
 *   //sampleStart
 *   val either = Either.conditionally(false, { "Always false" }, { throw RuntimeException("Will never execute") })
 *   val a = either.shouldBeLeft()
 *   val smartCasted: Left<String> = either
 *   //sampleEnd
 *   println(smartCasted)
 * }
 * ```
 */
@OptIn(ExperimentalContracts::class)
fun <A, B> Either<A, B>.shouldBeLeft(failureMessage: (B) -> String = { "Expected Either.Left, but found Either.Right with value $it" }): A {
   contract {
      returns() implies (this@shouldBeLeft is Either.Left<A>)
   }
   return when (this) {
      is Either.Left -> value
      is Either.Right -> throw AssertionError(failureMessage(value))
   }
}
