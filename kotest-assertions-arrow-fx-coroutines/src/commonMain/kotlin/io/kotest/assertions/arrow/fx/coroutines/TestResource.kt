package io.kotest.assertions.arrow.fx.coroutines

import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.Resource
import io.kotest.assertions.arrow.fx.coroutines.internal.allocatedCase
import io.kotest.mpp.atomics.AtomicReference
import kotlinx.coroutines.CompletableDeferred

/**
 * A [Resource] that is installed with either [ResourceExtension] or as a Project wide [Resource].
 * suspension is required to access the value of the [Resource].
 */
public interface TestResource<out A> {
  public val resource: Resource<A>
  public suspend operator fun invoke(): A
  public suspend fun get(): A = invoke()
}

internal class DefaultTestResource<A>(
  override val resource: Resource<A>, private val configure: A.() -> Unit = {}
) : TestResource<A> {
  sealed interface State<out A> {
    object Empty : State<Nothing>
    data class Loading<A>(
      val acquiring: CompletableDeferred<A> = CompletableDeferred(),
      val finalizers: CompletableDeferred<suspend (ExitCase) -> Unit> = CompletableDeferred()
    ) : State<A>

    data class Done<A>(val value: A, val finalizers: suspend (ExitCase) -> Unit) : State<A>
  }

  private val state = AtomicReference<State<A>>(State.Empty)

  override suspend operator fun invoke(): A = init()

  private tailrec fun swap(loading: State.Loading<A>, done: State.Done<A>): Unit =
    if (!state.compareAndSet(loading, done)) swap(loading, done) else Unit

  tailrec suspend fun init(): A = when (val current = state.value) {
    is State.Done -> current.value
    is State.Loading -> current.acquiring.await()
    State.Empty -> {
      val loading = State.Loading<A>()
      if (state.compareAndSet(State.Empty, loading)) {
        val (res, fin) = resource.allocatedCase()
        swap(loading, State.Done(res, fin))
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
  }
}
