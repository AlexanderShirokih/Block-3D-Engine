package ru.aleshi.block3d

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.lwjgl.opengl.GL11C
import ru.aleshi.block3d.input.Mouse
import ru.aleshi.block3d.internal.DispatchingRunnable
import ru.aleshi.block3d.internal.StubScene
import ru.aleshi.block3d.resources.ResourceList

/**
 * Root class for all engine hierarchy
 */
class World(
    /**
     * The window in which world will be drawn
     */
    val window: Window
) {

    internal val worldJob = Job()

    /**
     * Worlds coroutine scope
     */
    val worldScope = CoroutineScope(Dispatchers.Main + worldJob)

    /**
     * Current rendering dispatcher instance
     */
    internal val dispatcher: DispatchingRunnable = DispatchingRunnable(Runnable {
        // Clear the framebuffer
        GL11C.glClear(CLEAR_MASK)

        // Update the world
        update()

        // Update window events
        window.update()
    })

    /**
     * `true` until world isn't destroyed
     */
    var alive: Boolean = true
        private set

    companion object {
        private const val CLEAR_MASK = GL11C.GL_COLOR_BUFFER_BIT or GL11C.GL_DEPTH_BUFFER_BIT

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

        worldScope.launch {
            ResourceList.loadDefaultResources()

            // Launch start scene
            launchScene(startScene)
        }
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
     * Called internally to stop the current world
     */
    internal fun stop() {
        currentScene.stop()
        alive = false

        ResourceList.default.dispose()
        worldJob.cancel()
    }

    /**
     * Call to set the current scene.
     * It will destroy the current scene and loads new.
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