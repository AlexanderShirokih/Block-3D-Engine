package ru.aleshi.block3d

import ru.aleshi.block3d.input.Mouse
import ru.aleshi.block3d.internal.StubScene

/**
 * Root class for all engine hierarchy
 */
class World(
    /**
     * The window in which world will be drawn
     */
    val window: Window
) {
    /**
     * `true` until world isn't destroyed
     */
    var alive: Boolean = true
        private set

    companion object {
        lateinit var current: World
            private set
    }

    var currentScene: Scene = StubScene
        private set

    /**
     * Called internally when window size changed
     */
    internal fun resizeScenes(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            window.width = width
            window.height = height
            currentScene.resize(width, height)
        }
    }

    /**
     * Called internally when world is created
     */
    internal fun create(startScene: Scene) {
        window.setWindowResizeCallback(::resizeScenes)

        // Launch start scene
        launchScene(startScene)
    }

    /**
     * Saves instance of this world to [World.current]
     */
    fun makeCurrent() {
        current = this
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
            resize(window.width, window.height)
        }
    }
}