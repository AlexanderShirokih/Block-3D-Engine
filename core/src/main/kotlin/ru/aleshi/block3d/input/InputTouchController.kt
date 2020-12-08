package ru.aleshi.block3d.input

import ru.aleshi.block3d.types.Vector2f

interface InputTouchController {

    /**
     * Returns `true` is [button] is pressed
     */
    fun isButtonDown(button: MouseButton): Boolean

    /**
     * Returns current pointer position
     */
    fun getInputPosition(): Vector2f
}