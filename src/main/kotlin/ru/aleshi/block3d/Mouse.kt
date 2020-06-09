package ru.aleshi.block3d

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

    private var _hidden = false
    private var _grabbed = false

    var grabbed: Boolean
        get() = _grabbed
        set(value) {
            if (value != _grabbed && currentWindowHandle != 0L) {
                if (value)
                    glfwSetInputMode(currentWindowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
                else
                    glfwSetInputMode(currentWindowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
                _grabbed = value
            }
        }

    var hidden: Boolean
        get() = _hidden
        set(value) {
            if (value != _hidden && currentWindowHandle != 0L) {
                if (value)
                    glfwSetInputMode(currentWindowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)
                else
                    glfwSetInputMode(currentWindowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
                _hidden = value
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