package ru.aleshi.block3d

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.slf4j.LoggerFactory
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import ru.aleshi.block3d.internal.WindowConfig
import org.lwjgl.opengl.GL11C.*

/**
 * Entry point to start the engine. Creates GLFW window and manages it's state.
 */
object Launcher {
    private const val CLEAR_MASK = GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT
    private val logger = LoggerFactory.getLogger(Launcher::class.java)

    private var windowHandle: Long = 0

    /**
     * Creates the World instance and initializes GLFW window.
     */
    fun start(config: WindowConfig, startingScene: Scene): World {
        GLFWErrorCallback.createPrint(System.err).set()
        logger.info("Starting Block3D Engine")
        logger.info("LWJGL version: {}", Version.getVersion())
        logger.info("GLFW version: {}", glfwGetVersionString())

        return initWindowAndCreateWorld(config).apply {
            create(startingScene)
            //Render until the user attempts to close window or escape key pressed
            while (!glfwWindowShouldClose(windowHandle)) {
                // Clear framebuffer
                glClear(CLEAR_MASK)

                // Update the world
                update()

                glfwSwapBuffers(windowHandle)
                glfwPollEvents()
            }

            stop()
            destroyWindow()
        }
    }

    private fun initWindowAndCreateWorld(config: WindowConfig): World {
        if (!glfwInit()) {
            throw IllegalStateException("Cannot initialize GLFW window!")
        }

        // Create the window
        windowHandle = glfwCreateWindow(config.width, config.height, config.title, NULL, NULL)
        if (windowHandle == NULL)
            throw RuntimeException("Failed to create the GLFW window")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(
            GLFW_RESIZABLE,
            if (config.isResizable) GLFW_TRUE else GLFW_FALSE
        ) // the window will be resizable

        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            glfwGetWindowSize(windowHandle, pWidth, pHeight)
            val vidMode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!

            glfwSetWindowPos(
                windowHandle,
                (vidMode.width() - pWidth.get(0)) / 2,
                (vidMode.height() - pHeight.get(0)) / 2
            )
        }

        glfwMakeContextCurrent(windowHandle) // Make the OpenGL context current

        val caps = GL.createCapabilities()
        if (!caps.OpenGL20)
            throw RuntimeException("OpenGL 2.0 at least required to run the engine")

        val world = World().apply { setSize(config.width, config.height) }

        // Setup key and mouse callbacks
        glfwSetKeyCallback(
            windowHandle,
            Keyboard
        )
        glfwSetCursorPosCallback(
            windowHandle,
            Mouse
        )
        glfwSetFramebufferSizeCallback(windowHandle) { _, width, height -> world.setSize(width, height) }

        glfwSwapInterval(1) // V-sync
        glfwShowWindow(windowHandle)

        return world
    }

    private fun destroyWindow() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle)
        glfwDestroyWindow(windowHandle)

        // Terminate GLFW and free the error callback
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }
}