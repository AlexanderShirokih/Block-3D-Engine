package ru.aleshi.block3d

import ru.aleshi.block3d.internal.StubScene

/**
 * Root class for all engine hierarchy
 */
class World {
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

    private var currentScene: Scene = StubScene

    /**
     * Called internally when window size changed
     */
    internal fun setSize(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            this.width = width
            this.height = height
            currentScene.resize(width, height)
        }
    }

    /**
     * Called internally when world is created
     */
    internal fun create(startScene: Scene) {
        // Launch start scene
        launchScene(startScene)
    }

    /**
     * Called internally on each frame to update the world state
     */
    internal fun update() {
        Mouse.updateDelta()
        currentScene.update()
    }

    /**
     * Called internally to stop current world
     */
    internal fun stop() {
        currentScene.stop()
        alive = false
    }

    /**
     * Call to set the current scene.
     * It will destroy current scene and load new.
     */
    fun launchScene(scene: Scene) {
        currentScene.stop()
        currentScene = scene
        currentScene.apply {
            create()
            resize(width, height)
        }
    }
}