package ru.aleshi.block3d.input

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_REPEAT
import org.lwjgl.glfw.GLFWKeyCallback

/**
 * Global keyboard controller. Keeps all keys state.
 */
@Suppress("unused")
object Keyboard : GLFWKeyCallback() {
    private val printable = ByteArray(130)
    private val function = ByteArray(91)

    /**
     * When `true` closes window when escape key pressed.
     * By default `true`
     */
    var closeOnEscape: Boolean = true

    const val KEY_UNKNOWN = GLFW.GLFW_KEY_UNKNOWN
    const val KEY_SPACE = GLFW.GLFW_KEY_SPACE
    const val KEY_APOSTROPHE = GLFW.GLFW_KEY_APOSTROPHE
    const val KEY_COMMA = GLFW.GLFW_KEY_COMMA
    const val KEY_MINUS = GLFW.GLFW_KEY_MINUS
    const val KEY_PERIOD = GLFW.GLFW_KEY_PERIOD
    const val KEY_SLASH = GLFW.GLFW_KEY_SLASH
    const val KEY_0 = GLFW.GLFW_KEY_0
    const val KEY_1 = GLFW.GLFW_KEY_1
    const val KEY_2 = GLFW.GLFW_KEY_2
    const val KEY_3 = GLFW.GLFW_KEY_3
    const val KEY_4 = GLFW.GLFW_KEY_4
    const val KEY_5 = GLFW.GLFW_KEY_5
    const val KEY_6 = GLFW.GLFW_KEY_6
    const val KEY_7 = GLFW.GLFW_KEY_7
    const val KEY_8 = GLFW.GLFW_KEY_8
    const val KEY_9 = GLFW.GLFW_KEY_9
    const val KEY_SEMICOLON = GLFW.GLFW_KEY_SEMICOLON
    const val KEY_EQUAL = GLFW.GLFW_KEY_EQUAL
    const val KEY_A = GLFW.GLFW_KEY_A
    const val KEY_B = GLFW.GLFW_KEY_B
    const val KEY_C = GLFW.GLFW_KEY_C
    const val KEY_D = GLFW.GLFW_KEY_D
    const val KEY_E = GLFW.GLFW_KEY_E
    const val KEY_F = GLFW.GLFW_KEY_F
    const val KEY_G = GLFW.GLFW_KEY_G
    const val KEY_H = GLFW.GLFW_KEY_H
    const val KEY_I = GLFW.GLFW_KEY_I
    const val KEY_J = GLFW.GLFW_KEY_J
    const val KEY_K = GLFW.GLFW_KEY_K
    const val KEY_L = GLFW.GLFW_KEY_L
    const val KEY_M = GLFW.GLFW_KEY_M
    const val KEY_N = GLFW.GLFW_KEY_N
    const val KEY_O = GLFW.GLFW_KEY_O
    const val KEY_P = GLFW.GLFW_KEY_P
    const val KEY_Q = GLFW.GLFW_KEY_Q
    const val KEY_R = GLFW.GLFW_KEY_R
    const val KEY_S = GLFW.GLFW_KEY_S
    const val KEY_T = GLFW.GLFW_KEY_T
    const val KEY_U = GLFW.GLFW_KEY_U
    const val KEY_V = GLFW.GLFW_KEY_V
    const val KEY_W = GLFW.GLFW_KEY_W
    const val KEY_X = GLFW.GLFW_KEY_X
    const val KEY_Y = GLFW.GLFW_KEY_Y
    const val KEY_Z = GLFW.GLFW_KEY_Z
    const val KEY_LEFT_BRACKE = GLFW.GLFW_KEY_LEFT_BRACKET
    const val KEY_BACKSLASH = GLFW.GLFW_KEY_BACKSLASH
    const val KEY_RIGHT_BRACKET = GLFW.GLFW_KEY_RIGHT_BRACKET
    const val KEY_GRAVE_ACCENT = GLFW.GLFW_KEY_GRAVE_ACCENT
    const val KEY_WORLD_1 = GLFW.GLFW_KEY_WORLD_1
    const val KEY_WORLD_2 = GLFW.GLFW_KEY_WORLD_2
    const val KEY_ESCAPE = GLFW.GLFW_KEY_ESCAPE
    const val KEY_ENTER = GLFW.GLFW_KEY_ENTER
    const val KEY_TAB = GLFW.GLFW_KEY_TAB
    const val KEY_BACKSPACE = GLFW.GLFW_KEY_BACKSPACE
    const val KEY_INSERT = GLFW.GLFW_KEY_INSERT
    const val KEY_DELETE = GLFW.GLFW_KEY_DELETE
    const val KEY_RIGHT = GLFW.GLFW_KEY_RIGHT
    const val KEY_LEFT = GLFW.GLFW_KEY_LEFT
    const val KEY_DOWN = GLFW.GLFW_KEY_DOWN
    const val KEY_UP = GLFW.GLFW_KEY_UP
    const val KEY_PAGE_UP = GLFW.GLFW_KEY_PAGE_UP
    const val KEY_PAGE_DOWN = GLFW.GLFW_KEY_PAGE_DOWN
    const val KEY_HOME = GLFW.GLFW_KEY_HOME
    const val KEY_END = GLFW.GLFW_KEY_END
    const val KEY_CAPS_LOCK = GLFW.GLFW_KEY_CAPS_LOCK
    const val KEY_SCROLL_LOCK = GLFW.GLFW_KEY_SCROLL_LOCK
    const val KEY_NUM_LOCK = GLFW.GLFW_KEY_NUM_LOCK
    const val KEY_PRINT_SCREEN = GLFW.GLFW_KEY_PRINT_SCREEN
    const val KEY_PAUSE = GLFW.GLFW_KEY_PAUSE
    const val KEY_F1 = GLFW.GLFW_KEY_F1
    const val KEY_F2 = GLFW.GLFW_KEY_F2
    const val KEY_F3 = GLFW.GLFW_KEY_F3
    const val KEY_F4 = GLFW.GLFW_KEY_F4
    const val KEY_F5 = GLFW.GLFW_KEY_F5
    const val KEY_F6 = GLFW.GLFW_KEY_F6
    const val KEY_F7 = GLFW.GLFW_KEY_F7
    const val KEY_F8 = GLFW.GLFW_KEY_F8
    const val KEY_F9 = GLFW.GLFW_KEY_F9
    const val KEY_F10 = GLFW.GLFW_KEY_F10
    const val KEY_F11 = GLFW.GLFW_KEY_F11
    const val KEY_F12 = GLFW.GLFW_KEY_F12
    const val KEY_F13 = GLFW.GLFW_KEY_F13
    const val KEY_F14 = GLFW.GLFW_KEY_F14
    const val KEY_F15 = GLFW.GLFW_KEY_F15
    const val KEY_F16 = GLFW.GLFW_KEY_F16
    const val KEY_F17 = GLFW.GLFW_KEY_F17
    const val KEY_F18 = GLFW.GLFW_KEY_F18
    const val KEY_F19 = GLFW.GLFW_KEY_F19
    const val KEY_F20 = GLFW.GLFW_KEY_F20
    const val KEY_F21 = GLFW.GLFW_KEY_F21
    const val KEY_F22 = GLFW.GLFW_KEY_F22
    const val KEY_F23 = GLFW.GLFW_KEY_F23
    const val KEY_F24 = GLFW.GLFW_KEY_F24
    const val KEY_F25 = GLFW.GLFW_KEY_F25
    const val KEY_NUMPAD0 = GLFW.GLFW_KEY_KP_0
    const val KEY_NUMPAD1 = GLFW.GLFW_KEY_KP_1
    const val KEY_NUMPAD2 = GLFW.GLFW_KEY_KP_2
    const val KEY_KP_3 = GLFW.GLFW_KEY_KP_3
    const val KEY_NUMPAD4 = GLFW.GLFW_KEY_KP_4
    const val KEY_NUMPAD5 = GLFW.GLFW_KEY_KP_5
    const val KEY_NUMPAD6 = GLFW.GLFW_KEY_KP_6
    const val KEY_NUMPAD7 = GLFW.GLFW_KEY_KP_7
    const val KEY_NUMPAD8 = GLFW.GLFW_KEY_KP_8
    const val KEY_NUMPAD9 = GLFW.GLFW_KEY_KP_9
    const val KEY_KP_DECIMAL = GLFW.GLFW_KEY_KP_DECIMAL
    const val KEY_KP_DIVIDE = GLFW.GLFW_KEY_KP_DIVIDE
    const val KEY_KP_MULTIPLY = GLFW.GLFW_KEY_KP_MULTIPLY
    const val KEY_KP_SUBTRACT = GLFW.GLFW_KEY_KP_SUBTRACT
    const val KEY_KP_ADD = GLFW.GLFW_KEY_KP_ADD
    const val KEY_KP_ENTER = GLFW.GLFW_KEY_KP_ENTER
    const val KEY_KP_EQUAL = GLFW.GLFW_KEY_KP_EQUAL
    const val KEY_LSHIFT = GLFW.GLFW_KEY_LEFT_SHIFT
    const val KEY_LCONTROL = GLFW.GLFW_KEY_LEFT_CONTROL
    const val KEY_LALT = GLFW.GLFW_KEY_LEFT_ALT
    const val KEY_LSUPER = GLFW.GLFW_KEY_LEFT_SUPER
    const val KEY_RSHIFT = GLFW.GLFW_KEY_RIGHT_SHIFT
    const val KEY_RCONTROL = GLFW.GLFW_KEY_RIGHT_CONTROL
    const val KEY_RALT = GLFW.GLFW_KEY_RIGHT_ALT
    const val KEY_RSUPER = GLFW.GLFW_KEY_RIGHT_SUPER
    const val KEY_MENU = GLFW.GLFW_KEY_MENU

    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (action != GLFW_REPEAT) {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE && closeOnEscape)
                GLFW.glfwSetWindowShouldClose(window, true)
            if (key in KEY_SPACE..KEY_WORLD_2) {
                printable[key - KEY_SPACE] = (if (action == GLFW_PRESS) 1 else 0).toByte()
            } else if (key in KEY_ESCAPE..KEY_MENU) {
                function[key - KEY_ESCAPE] = (if (action == GLFW_PRESS) 1 else 0).toByte()
            }
        }
    }

    /**
     * Returns `true` if key currently pressed, false otherwise
     */
    fun isKeyDown(key: Int): Boolean {
        return when (key) {
            in KEY_SPACE..KEY_WORLD_2 -> printable[key - KEY_SPACE] == 1.toByte()
            in KEY_ESCAPE..KEY_MENU -> function[key - KEY_ESCAPE] == 1.toByte()
            else -> false
        }
    }
}