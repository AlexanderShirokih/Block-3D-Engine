package ru.aleshi.block3d.input

import ru.aleshi.block3d.types.Vector2f
import java.util.*

object Input {

    lateinit var inputController: InputTouchController
        internal set

    /**
     * Returns `true` is [button] is pressed
     */
    fun isButtonDown(button: MouseButton) = inputController.isButtonDown(button)

    /**
     * Returns current pointer position
     */
    fun getInputPosition(): Vector2f = inputController.getInputPosition()
}