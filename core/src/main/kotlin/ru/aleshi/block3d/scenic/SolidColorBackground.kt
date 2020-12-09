package ru.aleshi.block3d.scenic

import org.lwjgl.opengl.GL11C
import ru.aleshi.block3d.types.Color4f

/**
 * Fills scene background as solid color
 */
class SolidColorBackground(
    /**
     * Fill color
     */
    val color: Color4f
) : Background {

    constructor() : this(Color4f.black)

    override fun onApply() {
        GL11C.glDepthFunc(GL11C.GL_LESS)
        GL11C.glClearColor(color.red, color.green, color.blue, color.alpha)
    }

    override fun onSceneDrawn() = Unit

}