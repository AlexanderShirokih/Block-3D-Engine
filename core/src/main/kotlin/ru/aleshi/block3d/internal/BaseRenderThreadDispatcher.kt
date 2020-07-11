package ru.aleshi.block3d.internal

import kotlinx.coroutines.*
import kotlinx.coroutines.internal.MainDispatcherFactory
import ru.aleshi.block3d.World
import kotlin.coroutines.CoroutineContext

@InternalCoroutinesApi
sealed class BaseRenderThreadDispatcher : MainCoroutineDispatcher(), Delay {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        World.current.dispatcher.schedule(block)
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val cancellable = World.current.dispatcher.schedule(Runnable {
            with(continuation) { resumeUndispatched(Unit) }
        }, timeMillis)
        continuation.invokeOnCancellation { cancellable.cancel() }
    }

    @InternalCoroutinesApi
    internal class RenderThreadDispatcherFactory : MainDispatcherFactory {

        override val loadPriority: Int
            get() = 0

        override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher =
            RenderThreadDispatcher
    }

    private object ImmediateRenderThreadDispatcher : BaseRenderThreadDispatcher() {

        override val immediate: MainCoroutineDispatcher
            get() = this

        override fun toString() = "ImmediateRenderThreadDispatcher"
    }

    /**
     * Dispatches execution onto [DispatchingRunnable] that runs events in the render thread
     */
    internal object RenderThreadDispatcher : BaseRenderThreadDispatcher() {

        override val immediate: MainCoroutineDispatcher
            get() = ImmediateRenderThreadDispatcher

        override fun toString(): String = "RenderThreadDispatcher"
    }
}