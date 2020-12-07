package ru.aleshi.block3d.ui

import ru.aleshi.block3d.internal.WindowConfig
import ru.aleshi.block3d.types.Color4f

interface UIRenderContext {

    /**
     * Creates drawing context before scene started
     */
    fun createDrawingContext(config: WindowConfig)

    /**
     * Destroys drawing context after scene finishes
     */
    fun destroyDrawingContext()

    /**
     * Initiates UI rendering. Called before any object has drawn
     */
    fun beginFrame(width: Float, height: Float)

    /**
     * Finishes the frame rendering. called after all frames has drawn
     */
    fun endFrame()

    /**
     * Draws filled rect at position [x,y] with size [width, height]
     * and filling color `fillColor`
     */
    fun drawRect(x: Float, y: Float, width: Float, height: Float, fillColor: Color4f)
}