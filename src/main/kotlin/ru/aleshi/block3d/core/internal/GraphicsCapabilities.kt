package ru.aleshi.block3d.core.internal

/**
 * Contains info about current graphics capabilities
 */
data class GraphicsCapabilities(
    val openGL20: Boolean,
    val openGL30: Boolean
)