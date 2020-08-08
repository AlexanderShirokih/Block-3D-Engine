package ru.aleshi.block3d.scenic

import org.lwjgl.opengl.GL11C
import ru.aleshi.block3d.TextureCube
import ru.aleshi.block3d.internal.data.ImageCubeData
import ru.aleshi.block3d.primitives.SkyBox

/**
 * Fills scene background with [skyBoxMap] texture.
 */
class SkyBoxBackground(private val skyBoxMap: TextureCube) : Background {

    constructor(imageCubeData: ImageCubeData) : this(TextureCube(imageCubeData))

    private val skyBox = SkyBox()

    override fun onApply() {
        GL11C.glDepthFunc(GL11C.GL_LEQUAL)
        skyBox.material.setProperty("skyboxMap", skyBoxMap)
    }

    override fun onSceneDrawn() {
        skyBox.shader.apply {
            bind()
            skyBox.material.attach()
            skyBox.mesh.draw()
            unbind()
        }
    }

}