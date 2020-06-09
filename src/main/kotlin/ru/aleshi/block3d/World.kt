package ru.aleshi.block3d

import ru.aleshi.block3d.internal.GraphicsCapabilities

/**
 * Root class for all engine hierarchy
 */
class World(
    val graphicsCapabilities: GraphicsCapabilities
) {
    /**
     * `true` until world isn't destroyed
     */
    var alive: Boolean = true
        private set

    /**
     * Current window width
     */
    var width: Int = 0
        private set

    /**
     * Current window height
     */
    var height: Int = 0
        private set

    /**
     * Called internally when window size changed
     */
    fun setSize(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            this.width = width
            this.height = height
            //TODO: Notify scenes
        }
    }

    /**
     * Called internally on each frame to update the world state
     */
    fun update() {
        Mouse.updateDelta()
    }

    /**
     * Called internally to stop current world
     */
    fun stop() {
        //TODO: Stop the current scene and release resources
        alive = false

    }
}