package ru.aleshi.block3d.input

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorPosCallback

/**
 * Mouse controller.
 */
object Mouse : GLFWCursorPosCallback() {

    private var lastX = 0f
    private var lastY = 0f

    /**
     * Mouse delta X position from previous frame
     */
    var dx = 0f
        private set

    /**
     * Mouse delta Y position from previous frame
     */
    var dy = 0f
        private set

    /**
     * Current mouse X coordinate
     */
    var x = 0f
        private set

    /**
     * Current mouse Y coordinate
     */
    var y = 0f
        private set

    var grabbed: Boolean = false
        set(value) {
            if (value != field && currentWindowHandle != 0L) {
                if (value)
                    glfwSetInputMode(currentWindowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
                else
                    glfwSetInputMode(currentWindowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
                field = value
            }
        }

    var hidden: Boolean = false
        set(value) {
            if (value != field && currentWindowHandle != 0L) {
                if (value)
                    glfwSetInputMode(currentWindowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)
                else
                    glfwSetInputMode(currentWindowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
                field = value
            }
        }

    var currentWindowHandle: Long = 0L

    override fun invoke(window: Long, xpos: Double, ypos: Double) {
        currentWindowHandle = window
        x = xpos.toFloat()
        y = ypos.toFloat()
    }

    /**
     * Should be called only internally to update mouse deltas.
     */
    fun updateDelta() {
        dx = x - lastX
        dy = y - lastY
        lastX = x
        lastY = y
    }

}