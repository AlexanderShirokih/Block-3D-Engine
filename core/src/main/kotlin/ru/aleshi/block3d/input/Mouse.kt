package ru.aleshi.block3d.input

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorPosCallback
import ru.aleshi.block3d.types.Vector2f

/**
 * Mouse controller.
 */
object Mouse : GLFWCursorPosCallback(), InputTouchController {

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

    override fun getInputPosition(): Vector2f = Vector2f(x, y)

    /**
     * Returns `true` is [button] is pressed
     */
    override fun isButtonDown(button: MouseButton): Boolean {
        if (currentWindowHandle == 0L) return false
        val state = glfwGetMouseButton(currentWindowHandle, translateButtonCode(button))
        return state == GLFW_PRESS
    }

    private fun translateButtonCode(button: MouseButton): Int {
        return when (button) {
            MouseButton.LEFT -> GLFW_MOUSE_BUTTON_LEFT
            MouseButton.RIGHT -> GLFW_MOUSE_BUTTON_RIGHT
            MouseButton.MIDDLE -> GLFW_MOUSE_BUTTON_MIDDLE
        }
    }
}