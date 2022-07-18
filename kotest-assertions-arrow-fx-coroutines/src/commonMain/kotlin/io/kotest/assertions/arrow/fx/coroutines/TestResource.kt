package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.Resource
import io.kotest.assertions.arrow.fx.coroutines.internal.allocatedCase
import io.kotest.mpp.atomics.AtomicReference
import kotlinx.coroutines.CompletableDeferred
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.listeners.AfterSpecListener

/**
 * A [TestResource] represents a [Resource] that is _acquired_/_open_.
 *
 * - When using [ResourceExtension] it is tied to a [LifecycleMode]
 *   I.e. for [LifecycleMode.Spec] the [Resource] will be _open_ from [BeforeSpecListener.beforeSpec] until [AfterSpecListener.afterSpec].
 *
 * - When using [ProjectTestResource] it is tied to the entire life of the Kotest Project, and can be globally accessed from all [Spec] files.
 *
 * If you try access the _open_ resource outside its [LifecycleMode] it will result in [TestResource.AlreadyClosedException].
 */
public interface TestResource<out A> {
  public val resource: Resource<A>
  public suspend operator fun invoke(): A
  public suspend fun get(): A = invoke()

  public class AlreadyClosedException : IllegalStateException("Resource already closed and cannot be re-opened.")
}

internal class DefaultTestResource<A>(
  override val resource: Resource<A>, private val configure: A.() -> Unit = {}
) : TestResource<A> {
  sealed interface State<out A> {
    object Empty : State<Nothing>
    object Closed : State<Nothing>
    data class Loading<A>(
      val acquiring: CompletableDeferred<A> = CompletableDeferred(),
      val finalizers: CompletableDeferred<suspend (ExitCase) -> Unit> = CompletableDeferred()
    ) : State<A>

    data class Done<A>(val value: A, val finalizers: suspend (ExitCase) -> Unit) : State<A>
  }

  private val state = AtomicReference<State<A>>(State.Empty)

  override suspend operator fun invoke(): A = init()

  tailrec suspend fun init(): A = when (val current = state.value) {
    is State.Done -> current.value
    is State.Loading -> current.acquiring.await()
    is State.Closed -> throw TestResource.AlreadyClosedException()
    State.Empty -> {
      val loading = State.Loading<A>()
      if (state.compareAndSet(State.Empty, loading)) {
        val (res, fin) = resource.allocatedCase()
        state.value = State.Done(res, fin)
        loading.acquiring.complete(res.also(configure))
        loading.finalizers.complete(fin)
        res
      } else init()
    }
  }

  tailrec suspend fun release(): Unit = when (val current = state.value) {
    State.Empty -> Unit
    is State.Done -> if (state.compareAndSet(current, State.Empty)) current.finalizers(ExitCase.Completed)
    else release()

    is State.Loading -> if (state.compareAndSet(current, State.Empty)) current.finalizers.await()
      .invoke(ExitCase.Completed)
    else release()

    State.Closed -> Unit
  }

  fun close() {
    state.value = State.Closed
  }
}
