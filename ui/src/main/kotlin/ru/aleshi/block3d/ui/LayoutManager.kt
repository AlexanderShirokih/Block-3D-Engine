package ru.aleshi.block3d.ui

import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.widget.UIRootObject

/**
 * A class responsible for UI lay-out and rendering
 */
class LayoutManager(renderContext: UIRenderContext) {

    /**
     * Scene root object
     */
    val root: UIRootObject = UIRootObject(renderContext)

    /**
     * Updates layout measurements
     */
    fun measure(width: Float, height: Float) =
        root.measure(Constraint(maxSize = Vector2f(width, height)))

    /**
     * Draws layout on the screen
     * @param width current screen width in pixels
     * @param height current screen height in pixels
     */
    fun renderUI() =
        root.drawLayoutTree()
}