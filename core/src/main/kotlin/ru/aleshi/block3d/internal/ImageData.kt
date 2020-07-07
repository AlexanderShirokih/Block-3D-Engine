package ru.aleshi.block3d.internal

import java.nio.ByteBuffer

/**
 * A class for containing image data and it's color type (RGB or RGBA)
 */
class ImageData(
    val data: ByteBuffer,
    val width: Int,
    val height: Int,
    val hasAlpha: Boolean
)