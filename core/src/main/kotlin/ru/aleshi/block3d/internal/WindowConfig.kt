package ru.aleshi.block3d.internal

/**
 * Represents configuration for window setup.
 * @see ru.aleshi.block3d.core.Launcher.start
 */
data class WindowConfig(
    val width: Int = 800,
    val height: Int = 600,
    val title: String = "Block3D Engine",
    val isResizable: Boolean = true,
    val vSync: Boolean = true
)