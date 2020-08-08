package ru.aleshi.block3d

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.glfwGetVersionString
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.slf4j.LoggerFactory
import ru.aleshi.block3d.internal.GLFWWindow
import ru.aleshi.block3d.internal.WindowConfig
import ru.aleshi.block3d.scenic.Scene

/**
 * Entry point to start the engine. Creates GLFW window and manages its state.
 */
object Launcher {
    private val logger = LoggerFactory.getLogger(Launcher::class.java)

    /**
     * Creates the World instance and initializes GLFW window.
     */
    fun start(config: WindowConfig, startingScene: Scene): World {
        GLFWErrorCallback.createPrint(System.err).set()
        logger.info("Starting Block3D Engine")
        logger.info("LWJGL version: {}", Version.getVersion())
        logger.info("GLFW version: {}", glfwGetVersionString())

        return initWindowAndCreateWorld(config).apply {
            dispatcher.ownerThreadId = Thread.currentThread().id

            create(startingScene)

            //Render until the user attempts to close window or escape key pressed
            while (window.isRunning) {
                dispatcher.run()
            }

            stop()

            // Run estimated stopping tasks
            while (dispatcher.hasPendingEvents()) {
                dispatcher.runPendingEvents()
            }

            window.destroy()
        }
    }

    private fun initWindowAndCreateWorld(config: WindowConfig): World {
        val window = GLFWWindow().apply { create(config) }

        val caps = GL.createCapabilities()
        if (!caps.OpenGL30)
            throw RuntimeException("OpenGL 3.0 at least required to run the engine")

        return World(window).apply {
            makeCurrent()
            resizeScenes(config.width, config.height)
        }
    }

}