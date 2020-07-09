package ru.aleshi.block3d

import ru.aleshi.block3d.data.Image2DData
import ru.aleshi.block3d.data.ShaderData
import ru.aleshi.block3d.resources.Loader
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
        Material(Shared(Shader(Loader.loadResource("shaders/simple_unlit.shc") as ShaderData))).apply {
            setProperty("color", Color4f.grayscale(0.75f))
        }
    }

}