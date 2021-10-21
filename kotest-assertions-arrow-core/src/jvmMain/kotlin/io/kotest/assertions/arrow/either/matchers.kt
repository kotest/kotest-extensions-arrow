package io.kotest.assertions.arrow.either

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.types.beInstanceOf2
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Deprecated(
  "Use shouldBeRight from core",
  ReplaceWith("shouldBeRight()", "io.kotest.assertions.arrow.core.shouldBeRight")
)
@OptIn(ExperimentalContracts::class)
public fun <T> Either<*, T>.shouldBeRight() {
  contract {
    returns() implies (this@shouldBeRight is Either.Right<*>)
  }
  shouldBeRight()
}

@Deprecated(
  "Use shouldBeLeft from core",
  ReplaceWith("shouldBeLeft()", "io.kotest.assertions.arrow.core.shouldBeLeft")
)
@OptIn(ExperimentalContracts::class)
public fun <T> Either<T, Any?>.shouldBeLeft() {
  contract {
    returns() implies (this@shouldBeLeft is Either.Left<*>)
  }
  shouldBeLeft()
}

@Deprecated(
  "shouldNotBeRight is shouldBeLeft",
  ReplaceWith("shouldBeLeft()", "io.kotest.assertions.arrow.core.shouldBeLeft")
)
public fun <B> Either<Any?, B>.shouldNotBeRight(): Unit =
  shouldNot(beRight())

@Deprecated("Use shouldBeRight and shouldBe or shouldNotBe")
public fun <B> beRight(): Matcher<Either<Any?, B>> =
  beInstanceOf2<Either<Any?, B>, Either.Right<B>>()

@Deprecated("Use shouldBeRight and shouldBe or shouldNotBe")
public fun <B> beRight(b: B): Matcher<Either<Any?, B>> =
  object : Matcher<Either<Any?, B>> {
    override fun test(value: Either<Any?, B>): MatcherResult {
      return when (value) {
        is Either.Left -> {
          MatcherResult(
            false,
            "Either should be Right($b) but was Left(${value.value})",
            "Either should not be Right($b)"
          )
        }
        is Either.Right -> {
          if (value.value == b)
            MatcherResult(true, "Either should be Right($b)", "Either should not be Right($b)")
          else
            MatcherResult(
              false,
              "Either should be Right($b) but was Right(${value.value})",
              "Either should not be Right($b)"
            )
        }
      }
    }
  }

@Deprecated("Use shouldBeLeft and shouldBe or shouldNotBe")
public fun <A> beLeft(): Matcher<Either<A, Any?>> =
  beInstanceOf2<Either<A, Any?>, Either.Left<A>>()

@Deprecated("Use shouldBeLeft and shouldBe or shouldNotBe")
public fun <A> beLeft(a: A): Matcher<Either<A, Any?>> =
  object : Matcher<Either<A, Any?>> {
    override fun test(value: Either<A, Any?>): MatcherResult {
      return when (value) {
        is Either.Right -> {
          MatcherResult(
            false,
            "Either should be Left($a) but was Right(${value.value})",
            "Either should not be Right($a)"
          )
        }
        is Either.Left -> {
          if (value.value == a)
            MatcherResult(true, "Either should be Left($a)", "Either should not be Left($a)")
          else
            MatcherResult(
              false,
              "Either should be Left($a) but was Left(${value.value})",
              "Either should not be Right($a)"
            )
        }
      }
    }
  }

@Deprecated(
  "Convenience function is deprecated",
  ReplaceWith("fn(this.shouldBeRight())", "io.kotest.assertions.arrow.core.shouldBeRight")
)
public inline infix fun <B> Either<*, B>.shouldBeRight(fn: (B) -> Unit): Unit =
  fn(shouldBeRight())

@Deprecated(
  "Convenience function is deprecated",
  ReplaceWith("fn(this.shouldBeLeft())", "io.kotest.assertions.arrow.core.shouldBeLeft")
)
public inline infix fun <A> Either<A, *>.shouldBeLeft(fn: (A) -> Unit): Unit =
  fn(shouldBeLeft())

@Deprecated(
  "Use shouldBeRight from core",
  ReplaceWith(
    "shouldBeRight(b)",
    "io.kotest.assertions.arrow.core.shouldBeRight"
  )
)
public infix fun <B> Either<Any?, B>.shouldBeRight(b: B): Unit =
  shouldBe(beRight(b))

@Deprecated(
  "Use shouldBeLeft from core",
  ReplaceWith(
    "shouldBeLeft(a)",
    "io.kotest.assertions.arrow.core.shouldBeLeft"
  )
)
public infix fun <A> Either<A, Any?>.shouldBeLeft(a: A): Unit =
  shouldBe(beLeft(a))

@Deprecated(
  "Use core package",
  ReplaceWith(
    "shouldNotBeRight(b)",
    "io.kotest.assertions.arrow.core.shouldNotBeRight"
  )
)
public infix fun <B> Either<Any?, B>.shouldNotBeRight(b: B): Unit =
  shouldNot(beRight(b))

@Deprecated(
  "Use core package",
  ReplaceWith(
    "shouldNotBeLeft(b)",
    "io.kotest.assertions.arrow.core.shouldNotBeLeft"
  )
)
public infix fun <A> Either<A, Any?>.shouldNotBeLeft(a: A): Unit =
  shouldNot(beLeft(a))

@Deprecated(
  "Use shouldNotBeLeft from core",
  ReplaceWith("shouldNotBeLeft()", "io.kotest.assertions.arrow.core.shouldNotBeLeft")
)
public fun <A> Either<A, Any?>.shouldNotBeLeft(): Unit =
  shouldNot(beLeft())

@Deprecated(
  "Use shouldBeLeft",
  ReplaceWith(
    "shouldBeLeft().shouldBeTypeOf<A>()",
    "io.kotest.assertions.arrow.core.shouldBeLeft",
    "io.kotest.matchers.types.shouldBeTypeOf"
  )
)
public inline fun <reified A> Either<Any?, Any?>.shouldBeLeftOfType(): Unit =
  should(beLeftOfType<A>())

@Deprecated(
  "Use shouldBeLeft",
  ReplaceWith(
    "shouldBeLeft().shouldNotBeTypeOf<A>()",
    "io.kotest.assertions.arrow.core.shouldBeLeft",
    "io.kotest.matchers.types.shouldNotBeTypeOf"
  )
)
public inline fun <reified A> Either<Any?, Any?>.shouldNotBeLeftOfType(): Unit =
  shouldNot(beLeftOfType<A>())

@Deprecated("Use shouldBeLeft and either shouldNotBeOfType or shouldBeOfType directly")
public inline fun <reified A> beLeftOfType(): Matcher<Either<Any?, Any?>> =
  object : Matcher<Either<Any?, Any?>> {
    override fun test(value: Either<Any?, Any?>): MatcherResult {
      return when (value) {
        is Either.Right -> {
          MatcherResult(
            false,
            "Either should be Left<${A::class.qualifiedName}> but was Right(${value.value})",
            ""
          )
        }
        is Either.Left -> {
          val valueA = value.value
          if (valueA is A)
            MatcherResult(true, "Either should be Left<${A::class.qualifiedName}>", "Either should not be Left")
          else
            MatcherResult(
              false,
              "Either should be Left<${A::class.qualifiedName}> but was Left<${if (valueA == null) "Null" else valueA::class.qualifiedName}>",
              "Either should not be Left"
            )
        }
      }
    }
  }
