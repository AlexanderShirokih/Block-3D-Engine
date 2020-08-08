package ru.aleshi.block3d

import ru.aleshi.block3d.resources.ResourceList
import ru.aleshi.block3d.types.Color4f

/**
 * Contains instances for default resources.
 * Don't modify directly these resources without creating their copies!
 */
object Defaults {

    /**
     * White texture with size 1x1 pixels
     */
    val TEXTURE_WHITE by lazy {
        Texture2D.ofColor(Color4f.white)
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
     *  ambientColor  : Ambient color . Default [Color4f.grayscale]`(0.15f)`
     *  specularColor : Specular color. Default [Color4f.grayscale]`(0.5f)`
     *  reflectance   : Specular factor power. Default `10f`.
     */
    val MATERIAL_LIT by lazy {
        Material(ResourceList.default.requireShader("Simple/Lit")).apply {
            setProperty("ambientColor", Color4f.grayscale(0.15f))
            setProperty("specularColor", Color4f.grayscale(0.5f))
            setProperty("reflectance", 10f)
        }
    }

    /**
     * Material which is used to draw simple skybox.
     * Properties:
     *  skyboxMap   : Cubemap containing background.
     */
    val MATERIAL_SKYBOX by lazy {
        Material(ResourceList.default.requireShader("Skybox/CubemapSkybox"))
    }

}