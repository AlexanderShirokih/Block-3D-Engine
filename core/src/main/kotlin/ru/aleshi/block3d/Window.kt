package ru.aleshi.block3d

import ru.aleshi.block3d.internal.WindowConfig

/**
 * A class describing program window
 *
 */
abstract class Window(val config: WindowConfig) {

    /**
     * `true` while window is not going to be destroyed
     */
    abstract val isRunning: Boolean

    /**
     * Current window width
     */
    var width: Int = 0

    /**
     * Current window height
     */
    var height: Int = 0

    /**
     * Used to create window instance
     */
    abstract fun create()

    /**
     * Used to close window and dispose it's resources
     */
    abstract fun destroy()

    /**
     * Sets the resize callback
     */
    abstract fun setWindowResizeCallback(resizeCallback: (width: Int, height: Int) -> Unit)

    /**
     * Call to update window info. For instance swap buffers or pull events
     */
    abstract fun update()
}