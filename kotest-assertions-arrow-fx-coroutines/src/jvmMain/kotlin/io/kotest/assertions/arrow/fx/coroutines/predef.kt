package io.kotest.assertions.arrow.fx.coroutines

import arrow.continuations.generic.AtomicRef
import java.util.concurrent.ThreadFactory

public val threadName: suspend () -> String =
  { Thread.currentThread().name }

public class NamedThreadFactory(private val mkName: (Int) -> String) : ThreadFactory {
  private val count = AtomicRef(0)
  override fun newThread(r: Runnable): Thread =
    Thread(r, mkName(count.get()))
      .apply { isDaemon = true }
}
