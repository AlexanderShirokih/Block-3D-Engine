package ru.aleshi.block3d.types

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
        val white = Color4f(1f, 1f, 1f)
        val red = Color4f(1f, 0f, 0f)
        val green = Color4f(0f, 1f, 0f)
        val blue = Color4f(0f, 0f, 1f)
        val magenta = Color4f(1f, 0f, 1f)
    }
}