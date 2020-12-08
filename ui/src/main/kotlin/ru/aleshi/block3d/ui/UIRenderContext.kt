package ru.aleshi.block3d.ui

import ru.aleshi.block3d.internal.WindowConfig
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector2f

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

    /**
     * Draws text at position [x, y]
     */
    fun drawText(x: Float, y: Float, rowWidth: Float, fontSize: Float, color: Color4f, text: String)

    /**
     * Measures [text] and returns its bounds
     */
    fun measureText(rowWidth: Float, fontSize: Float, text: String): Vector2f

    /**
     * Asynchronously init required resources
     */
    suspend fun initResources() {}
}