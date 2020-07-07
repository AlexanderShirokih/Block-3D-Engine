package ru.aleshi.block3d

import java.util.concurrent.atomic.AtomicInteger

/**
 * Used to manage resource instances
 */
class Shared<T : IDisposable>(private val instance: T) {

    private val refCounter = AtomicInteger(0)

    /**
     * Increments reference count
     */
    fun getAndInc(): T {
        refCounter.incrementAndGet()
        return instance
    }

    /**
     * Decrements reference count. If reference count reaches zero, it will be disposed
     */
    fun decRef() {
        if (refCounter.decrementAndGet() == 0)
            instance.dispose()
    }
}