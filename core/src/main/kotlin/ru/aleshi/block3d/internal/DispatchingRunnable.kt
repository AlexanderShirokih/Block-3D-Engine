package ru.aleshi.block3d.internal

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * A runnable that can run repeatedly [mainTask] and also dispatched events.
 */
class DispatchingRunnable(private val mainTask: Runnable) : Runnable {

    /**
     * The main task will updates while this flag it's
     */
    var running: Boolean = true

    data class DelayedTask(
        val runnable: Runnable,
        val startTime: Long
    ) {

        @JvmField
        @Volatile
        var cancelled: Boolean = false
    }

    /**
     * Cancellation callback
     */
    class Cancellable(private val dispatchingRunnable: DispatchingRunnable, private val delayedTask: DelayedTask) {

        /**
         * Cancels execution of scheduled delayed task it it's not started yet
         */
        fun cancel() {
            delayedTask.cancelled = true
        }
    }

    private val eventQueue = ConcurrentLinkedQueue<DelayedTask>()

    internal var ownerThreadId: Long = 0

    /**
     * Adds this runnable to event queue. It will be executed as soon as all pending events will
     * executed and main runnable task was completed.
     */
    fun schedule(runnable: Runnable) {
        if (Thread.currentThread().id == ownerThreadId)
            runnable.run()
        else
            eventQueue.add(DelayedTask(runnable, 0L))
    }

    /**
     * Schedules for running [runnable]. It will be run not before [startTime]
     */
    fun schedule(runnable: Runnable, startTime: Long): Cancellable {
        val task = DelayedTask(runnable, startTime)
        eventQueue.add(task)

        return Cancellable(this, task)
    }

    override fun run() {
        runPendingEvents()

        // Run the main task
        mainTask.run()
    }

    internal fun hasPendingEvents() = !eventQueue.isEmpty()

    internal fun runPendingEvents() {
        val startTime = System.currentTimeMillis()

        // Run all pending events
        for (event in eventQueue) {
            if (event.startTime <= startTime && !event.cancelled)
                event.runnable.run()
        }

        // Remove all completed events
        eventQueue.removeIf { it.cancelled || it.startTime <= startTime }
    }

}