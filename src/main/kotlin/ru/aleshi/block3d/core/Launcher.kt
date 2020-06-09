package ru.aleshi.block3d.core

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.slf4j.LoggerFactory
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import ru.aleshi.block3d.core.internal.WindowConfig
import org.lwjgl.opengl.GL11C.*

/**
 * Entry point to start the engine. Creates GLFW window and manages it's state.
 */
object Launcher {
    private val logger = LoggerFactory.getLogger(Launcher::class.java)

    private var windowHandle: Long = 0

    /**
     * Entry point
     */
    @JvmStatic
    fun main(args: Array<String>) {
        //TODO: Parse cmd args to WindowConfig
        // Start window with default config
        start(WindowConfig())
    }

    /**
     * Initializes the game engine and GLFW window.
     */
    fun start(config: WindowConfig) {
        GLFWErrorCallback.createPrint(System.err).set();
        logger.info("Starting Block3D Engine")
        logger.info("LWJGL version: {}", Version.getVersion())
        logger.info("GLFW version: {}", glfwGetVersionString())

        initWindow(config)
        loop()
        destroyWindow()
    }

    private fun initWindow(config: WindowConfig) {
        if (!glfwInit()) {
            throw IllegalStateException("Cannot initialize GLFW window!")
        }

        // Create the window
        windowHandle = glfwCreateWindow(config.width, config.height, config.title, NULL, NULL)
        if (windowHandle == NULL)
            throw RuntimeException("Failed to create the GLFW window")

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(
            GLFW_RESIZABLE,
            if (config.isResizable) GLFW_TRUE else GLFW_FALSE
        ) // the window will be resizable

        // Setup a key callback
        glfwSetKeyCallback(windowHandle, object : GLFWKeyCallback() {
            override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        })

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
        glfwSwapInterval(1) // V-sync
        glfwShowWindow(windowHandle)
    }

    private fun loop() {
        GL.createCapabilities()

        glClearColor(1f, 0f, 1f, 1f)

        //Render until the user attempts to close window or escape key pressed
        while (!glfwWindowShouldClose(windowHandle)) {
            // Clear framebuffer
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            //TODO: Engine goes here

            glfwSwapBuffers(windowHandle)
            glfwPollEvents()
        }
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