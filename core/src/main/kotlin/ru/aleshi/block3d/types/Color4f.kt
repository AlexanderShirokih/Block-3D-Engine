package ru.aleshi.block3d.types

import ru.aleshi.block3d.clamp
import kotlin.random.Random

/**
 * Represents RGBA colors in floating point values
 */
data class Color4f(
    var red: Float = 0f,
    var green: Float = 0f,
    var blue: Float = 0f,
    var alpha: Float = 1f
) {
    companion object {
        val black = Color4f(0f, 0f, 0f)
        val gray = Color4f(0.5f, 0.5f, 0.5f)
        val yellow = Color4f(1f, 1f, 0f)
        val white = Color4f(1f, 1f, 1f)
        val red = Color4f(1f, 0f, 0f)
        val green = Color4f(0f, 1f, 0f)
        val blue = Color4f(0f, 0f, 1f)
        val magenta = Color4f(1f, 0f, 1f)
        val purple = Color4f(0.5f, 0f, 0.5f)

        val random: Color4f
            get() = Color4f(Random.Default.nextFloat(), Random.Default.nextFloat(), Random.Default.nextFloat(), 1f)

        /**
         * Returns grayscale color where value=0.0f means completely black, value=1.0f means white
         */
        fun grayscale(value: Float): Color4f {
            val clamped = value.clamp(0f, 1f)
            return Color4f(clamped, clamped, clamped, 1f)
        }
    }
}