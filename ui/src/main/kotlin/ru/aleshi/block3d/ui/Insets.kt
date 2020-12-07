package ru.aleshi.block3d.ui

/**
 *  Defined additional space used as padding from container border
 */
data class Insets(
    val left: Float = 0.0f,
    val right: Float = 0.0f,
    val top: Float = 0.0f,
    val bottom: Float = 0.0f
) {

    /**
     * Creates new [Insets] with symmetrical paddings
     */
    constructor(horizontal: Float = 0f, vertical: Float = 0f) : this(horizontal, horizontal, vertical, vertical)

    /**
     * Creates new [Insets] that has the same padding on all sided
     */
    constructor(value: Float) : this(value, value, value, value)

    val width: Float
        get() = left + right

    val height: Float
        get() = top + bottom
}