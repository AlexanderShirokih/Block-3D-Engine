package ru.aleshi.block3d.debug

/**
 * Utility class used to measure FPS.
 */
class FPSCounter {

    /**
     * Current FPS value
     */
    var fps = 0
        private set

    private var fpsCount = 0
    private var prevFrameTime: Long = System.nanoTime()

    /**
     * Call in update cycle to update measurement
     */
    fun update() {
        fpsCount++

        val currentTime = System.nanoTime()
        val delta = currentTime - prevFrameTime

        if (delta > 1000_000_000L) {
            fps = fpsCount
            fpsCount = 0
            prevFrameTime = currentTime
        }

    }
}