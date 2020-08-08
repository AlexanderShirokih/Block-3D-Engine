package ru.aleshi.block3d.data

import kotlinx.serialization.Serializable
import ru.aleshi.block3d.types.Side

/**
 * Describes file/resource paths to load cubemap image data
 */
@Serializable
data class ImageCubeSource(val faceImagePaths: Map<Side, String>)