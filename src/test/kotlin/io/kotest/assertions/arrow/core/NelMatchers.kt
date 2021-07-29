package io.kotest.assertions.arrow.core

import arrow.core.NonEmptyList
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot

class NelMatchers : WordSpec() {

  init {

    "containNull()" should {
      "test that a nel contains at least one null" {
        NonEmptyList(1, listOf(2, null)) should containNull()
        NonEmptyList(null, listOf()) should containNull()
        NonEmptyList(1, listOf(2)) shouldNot containNull()

        NonEmptyList(null, listOf()).shouldContainNull()
        NonEmptyList(1, listOf()).shouldNotContainNull()
      }
    }

    "haveElementAt()" should {
      "test that a nel contains an element at the right position" {
        NonEmptyList(1, listOf(2, null)) should haveElementAt(1, 2)
        NonEmptyList(1, listOf(2, null)).shouldContainElementAt(1, 2)
        NonEmptyList(1, listOf(2, null)).shouldNotContainElementAt(0, 42)
      }
    }

    "sorted" should {
      "test that a collection is sorted" {
        NonEmptyList(1, listOf(2, 3, 4)) should beSorted()
        NonEmptyList(1, listOf(2, 3, 4)).shouldBeSorted()
        shouldThrow<AssertionError> {
          NonEmptyList(2, listOf(1)) should beSorted()
        }
      }

      "test that a collection is not sorted" {
        NonEmptyList(3, listOf(2, 1, 4)) shouldNot beSorted()
        NonEmptyList(5, listOf(2, 3, 4)).shouldNotBeSorted()
      }
    }

    "haveDuplicates" should {
      "test that a collection is unique or not" {
        NonEmptyList(1, listOf(2, 3, 3)) should haveDuplicates()
        NonEmptyList(1, listOf(2, 3, 4)) shouldNot haveDuplicates()
        NonEmptyList(1, listOf(2, 3, 3)).shouldHaveDuplicates()
        NonEmptyList(1, listOf(2, 3, 4)).shouldNotHaveDuplicates()
      }
    }

    "beUnique" should {
      "test that a collection is unique or not" {
        NonEmptyList(1, listOf(2, 3, 4)).shouldBeUnique()
        NonEmptyList(1, listOf(2, 3, 3)).shouldNotBeUnique()
      }
    }

    "singleElement" should {
      "test that a collection contains a single given element"  {
        NonEmptyList(1, listOf()) shouldBe singleElement(1)
        shouldThrow<AssertionError> {
          NonEmptyList(1, listOf()) shouldBe singleElement(2)
        }
        shouldThrow<AssertionError> {
          NonEmptyList(1, listOf(2)) shouldBe singleElement(2)
        }

        NonEmptyList(1, listOf()) shouldBeSingleElement 1
      }

      "test that a collection does not contain a single element"  {
        NonEmptyList(1, listOf(2)) shouldNotBeSingleElement 1
      }
    }

    "should contain element" should {
      "test that a collection contains an element"  {
        NonEmptyList(1, listOf(2, 3)) should contain(2)
        NonEmptyList(1, listOf(2, 3)) shouldContain 2
        NonEmptyList(1, listOf(2, 3)) shouldNotContain 4
        shouldThrow<AssertionError> {
          NonEmptyList(1, listOf(2, 3)) should contain(4)
        }
      }
    }

    "haveSize" should {
      "test that a collection has a certain size" {
        NonEmptyList(1, listOf(2, 3)) should haveSize(3)
        NonEmptyList(1, listOf(2, 3)) shouldHaveSize 3
        NonEmptyList(1, listOf(2, 3)) shouldNotHaveSize 2
        shouldThrow<AssertionError> {
          NonEmptyList(1, listOf(2, 3)) should haveSize(2)
        }
      }
    }

    "containNoNulls" should {
      "test that a collection contains zero nulls"  {
        NonEmptyList(1, listOf(2, 3)) should containNoNulls()
        NonEmptyList(1, listOf(2, 3)).shouldContainNoNulls()
        NonEmptyList(null, listOf(null, null)) shouldNot containNoNulls()
        NonEmptyList(1, listOf(null, null)) shouldNot containNoNulls()
        NonEmptyList(1, listOf(null, null)).shouldNotContainNoNulls()
      }
    }

    "containOnlyNulls" should {
      "test that a collection contains only nulls"  {
        NonEmptyList(null, listOf(null, null)) should containOnlyNulls()
        NonEmptyList(null, listOf(null, null)).shouldContainOnlyNulls()
        NonEmptyList(1, listOf(null, null)) shouldNot containOnlyNulls()
        NonEmptyList(1, listOf(2, 3)) shouldNot containOnlyNulls()
        NonEmptyList(1, listOf(2, 3)).shouldNotContainOnlyNulls()
      }
    }

    "containsAll" should {
      "test that a collection contains all the elements but in any order" {
        val col = NonEmptyList(1, listOf(2, 3, 4, 5))

        col should containAll(1, 2, 3)
        col should containAll(3, 2, 1)
        col should containAll(5, 1)
        col should containAll(1, 5)
        col should containAll(1)
        col should containAll(5)

        col.shouldContainAll(1, 2, 3, 4)
        col.shouldContainAll(1, 2, 3, 4, 5)
        col.shouldContainAll(3, 2, 1)
        col.shouldContainAll(5, 4, 3, 2, 1)
        col shouldContainAll listOf(1, 2, 3, 4)

        shouldThrow<AssertionError> {
          col should containAll(1, 2, 6)
        }

        shouldThrow<AssertionError> {
          col should containAll(6)
        }

        shouldThrow<AssertionError> {
          col should containAll(0, 1, 2)
        }

        shouldThrow<AssertionError> {
          col should containAll(3, 2, 0)
        }
      }

      "test that a collection shouldNot containAll elements" {
        val col = NonEmptyList(1, listOf(2, 3, 4, 5))

        col shouldNot containAll(99, 88, 77)

        col.shouldNotContainAll(99,88,77)
        col shouldNotContainAll listOf(99, 88, 77)
      }
    }
  }
}
