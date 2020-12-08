package ru.aleshi.block3d.ui

/**
 * Describes alignment constraints
 * -1 means left or top
 *  1 means right or bottom
 *  0 means center
 */
class Alignment(
    val xAlignment: Float,
    val yAlignment: Float
) {
    companion object {
        @JvmField
        val leftCenter = Alignment(-1f, 0f)

        @JvmField
        val rightCenter = Alignment(1f, 0f)

        @JvmField
        val leftTop = Alignment(-1f, -1f)

        @JvmField
        val rightTop = Alignment(1f, -1f)

        @JvmField
        val leftBottom = Alignment(-1f, 1f)

        @JvmField
        val rightBottom = Alignment(1f, 1f)

        @JvmField
        val center = Alignment(0f, 0f)

        @JvmField
        val topCenter = Alignment(0f, -1f)

        @JvmField
        val bottomCenter = Alignment(0f, 1f)
    }
}