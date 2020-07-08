package ru.aleshi.block3d

import org.lwjgl.opengl.GL11

/**
 * Common class for all texture kinds
 * @see Texture2D
 */
abstract class Texture internal constructor(val glType: Int) : IDisposable {

    internal var texId: Int = GL11.glGenTextures()
        private set

    override fun dispose() {
        if (texId != 0) {
            GL11.glDeleteTextures(texId)
            texId = 0
        }
    }
}