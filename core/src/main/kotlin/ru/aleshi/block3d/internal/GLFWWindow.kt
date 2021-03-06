package ru.aleshi.block3d.internal

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.Block3DException
import ru.aleshi.block3d.Window
import ru.aleshi.block3d.input.Input
import ru.aleshi.block3d.input.Keyboard
import ru.aleshi.block3d.input.Mouse

/**
 * Describes GLFW Window implementation
 */
class GLFWWindow(config: WindowConfig) : Window(config) {

    private var windowHandle: Long = 0

    override fun create() {
        if (!glfwInit()) {
            throw IllegalStateException("Cannot initialize GLFW window!")
        }

        // Create the window
        windowHandle =
            glfwCreateWindow(config.width, config.height, config.title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (windowHandle == MemoryUtil.NULL)
            throw Block3DException("Failed to create the GLFW window")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(
            GLFW_RESIZABLE,
            if (config.isResizable) GLFW_TRUE else GLFW_FALSE
        ) // the window will be resizable

        // Enable Antialiasing
        if (config.antialiasing) {
            glfwWindowHint(GLFW_SAMPLES, 4)
        }

        MemoryStack.stackPush().use { stack ->
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

        // Setup key and mouse callbacks
        glfwSetKeyCallback(
            windowHandle,
            Keyboard
        )
        glfwSetCursorPosCallback(
            windowHandle,
            Mouse
        )

        Input.inputController = Mouse

        glfwSwapInterval(if (config.vSync) 1 else 0) // V-sync

        glfwShowWindow(windowHandle)
    }

    override fun update() {
        glfwSwapBuffers(windowHandle)
        glfwPollEvents()
    }

    override fun destroy() {
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(windowHandle)
        glfwDestroyWindow(windowHandle)

        // Terminate GLFW and free the error callback
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }

    override fun setWindowResizeCallback(resizeCallback: (width: Int, height: Int) -> Unit) {
        glfwSetFramebufferSizeCallback(windowHandle) { _, width, height -> resizeCallback(width, height) }
    }

    override val isRunning: Boolean
        get() = !glfwWindowShouldClose(windowHandle)
}