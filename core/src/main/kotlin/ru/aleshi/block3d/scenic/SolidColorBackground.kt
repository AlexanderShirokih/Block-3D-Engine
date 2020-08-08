package ru.aleshi.block3d.scenic

import org.lwjgl.opengl.GL11C
import ru.aleshi.block3d.types.Color4f

/**
 * Fills scene background as solid color
 */
class SolidColorBackground : Background {

    /**
     * Fill color
     */
    var color: Color4f = Color4f.black
        set(value) {
            field = value
            onApply()
        }

    override fun onApply() {
        GL11C.glDepthFunc(GL11C.GL_LESS)
        GL11C.glClearColor(color.red, color.green, color.blue, color.alpha)
    }

    override fun onSceneDrawn() = Unit

}