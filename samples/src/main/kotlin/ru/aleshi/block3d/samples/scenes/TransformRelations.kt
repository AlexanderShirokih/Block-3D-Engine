package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Defaults
import ru.aleshi.block3d.Texture2D
import ru.aleshi.block3d.World
import ru.aleshi.block3d.internal.data.Image2DData
import ru.aleshi.block3d.primitives.Sphere
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.scenic.TransformableObject
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f
import ru.aleshi.block3d.ui.ui

/**
 * This example shows work of relative transformations in object hierarchy
 */
class TransformRelations : Scene() {

    var sun: TransformableObject? = null
    var earth: TransformableObject? = null
    lateinit var moon: TransformableObject

    private val rotation = Quaternion.fromAxisAngle(Vector3f.up, 0.2f)

    override fun create() {
        super.create()
        background = SolidColorBackground(Color4f.grayscale(0.13f))
        Camera.active.transform.position = Vector3f.forward * 60f
        Camera.active.transform.rotation = Quaternion.fromAxisAngle(Vector3f.right, -35f)

        sceneScope.launch {
            val sunTexture = Loader.loadResource("textures/sun.png") as Image2DData
            val earthTexture = Loader.loadResource("textures/earth.png") as Image2DData
            val moonTexture = Loader.loadResource("textures/moon.png") as Image2DData

            sun = Sphere().apply {
                add(this)
                transform.scale = Vector3f(10f)
                material.setProperty("mainTexture", Texture2D(sunTexture))
                material.setProperty("specularColor", Color4f.white)
                material.setProperty("ambientColor", Color4f.white)
            }

            earth = Sphere().apply {
                add(this, sun)
                transform.scale = Vector3f(.5f)
                transform.position = Vector3f.right * 3f
                material.setProperty("mainTexture", Texture2D(earthTexture))
                material.setProperty("ambientColor", Color4f.white)
            }

            moon = Sphere(Defaults.MATERIAL_UNLIT).apply {
                add(this, earth)
                transform.scale = Vector3f(0.3f)
                transform.position = Vector3f.forward * 1.5f
                material.setProperty("mainTexture", Texture2D(moonTexture))
            }

            ui.setContent(
                createBackButton {
                    World.current.launchSceneAsync(MainScene())
                }
            )
        }
    }

    override fun update() {
        super.update()
        sun?.transform?.rotate(rotation)
        earth?.transform?.rotate(rotation * rotation)
    }
}