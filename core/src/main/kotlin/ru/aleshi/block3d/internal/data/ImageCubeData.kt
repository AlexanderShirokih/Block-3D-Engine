package ru.aleshi.block3d.internal.data

import ru.aleshi.block3d.types.Side

/**
 * A class map of [Image2DData] for each cubemap face.
 */
class ImageCubeData(val data: Map<Side, Image2DData>) {

    /**
     * `true` if any face texture [generateMipmaps] returns `true`
     */
    val generateMipmaps: Boolean
        get() = data.any { it.value.generateMipmaps }
}