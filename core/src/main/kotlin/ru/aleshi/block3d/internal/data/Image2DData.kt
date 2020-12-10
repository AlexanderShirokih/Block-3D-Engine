package ru.aleshi.block3d.internal.data

import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

/**
 * A class for containing image data and it's color type (RGB or RGBA)
 */
class Image2DData(
    val data: ByteBuffer,
    val width: Int,
    val height: Int,
    val hasAlpha: Boolean,
    val generateMipmaps: Boolean = true
) {

    var isRecycled: Boolean = false
        private set

    /**
     * Deallocates allocated memory
     */
    fun recycle() {
        if (!isRecycled) {
            MemoryUtil.memFree(data)
            isRecycled = true
        }
    }

}