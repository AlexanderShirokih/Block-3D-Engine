package ru.aleshi.block3d.debug

import ru.aleshi.block3d.types.Color4f

/**
 * Common class for debugging draw objects
 */
abstract class DebugObjectDrawer(val color: Color4f, internal val keepInScene: Boolean = false) {

    /**
     * Called to draw the object
     */
    abstract fun draw()

    /**
     * Disposes object resources
     */
    abstract fun dispose()

}