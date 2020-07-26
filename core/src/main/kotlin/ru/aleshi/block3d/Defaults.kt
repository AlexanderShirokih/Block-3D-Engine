package ru.aleshi.block3d

import ru.aleshi.block3d.data.Image2DData
import ru.aleshi.block3d.resources.ResourceList
import ru.aleshi.block3d.types.Color4f
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * Contains instances for default resources.
 * Don't modify directly these resources without creating their copies!
 */
object Defaults {

    /**
     * White texture with size 1x1 pixels
     */
    val TEXTURE_WHITE by lazy {
        Texture2D(
            Image2DData(
                data = (ByteBuffer.allocateDirect(4).putInt(0xFFFFFFFF.toInt()) as Buffer).flip() as ByteBuffer,
                width = 1,
                height = 1,
                hasAlpha = true,
                generateMipmaps = false
            )
        )
    }

    /**
     * Simple textured unlit material.
     * Properties:
     *  mainTexture : Primary texture. Default [Defaults.TEXTURE_WHITE]
     *  color       : Main color. Default 0.75 grayscale
     */
    val MATERIAL_UNLIT by lazy {
        Material(ResourceList.default.requireShader("Simple/Unlit")).apply {
            setProperty("color", Color4f.grayscale(0.75f))
        }
    }

    /**
     * Simple texture lit material.
     * Properties:
     *  mainTexture   : Primary texture. Default [Defaults.TEXTURE_WHITE]
     *  ambientColor  : Ambient color . Default [Color4f.grayscale]`(0.25f)`
     *  specularColor : Specular color. Default [Color4f.grayscale]`(0.5f)`
     *  reflectance   : Specular factor power. Default `10f`.
     */
    val MATERIAL_LIT by lazy {
        Material(ResourceList.default.requireShader("Simple/Lit")).apply {
            setProperty("ambientColor", Color4f.grayscale(0.25f))
            setProperty("specularColor", Color4f.grayscale(0.5f))
            setProperty("reflectance", 10f)
        }
    }

}