package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.Resource

class ResourceUnderTest {
  var count: Int = 0
  var isOpen: Boolean = false
    private set
  var isReleased: Boolean = false
    private set
  var isConfigured: Boolean = false
    private set

  fun configure(): Unit {
    isConfigured = true
  }

  fun asResource(): Resource<ResourceUnderTest> =
    Resource(
      {
        isReleased = false
        isOpen = true
        count += 1
        this
      },
      { res, _ ->
        res.isOpen = false
        res.isConfigured = false
        res.isReleased = true
      }
    )
}
