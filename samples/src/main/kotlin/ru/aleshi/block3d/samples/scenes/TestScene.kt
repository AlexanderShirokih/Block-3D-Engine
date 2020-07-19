package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.Scene
import ru.aleshi.block3d.TransformableObject
import ru.aleshi.block3d.lights.PointLight
import ru.aleshi.block3d.primitives.Sphere
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class TestScene : Scene() {

    private var model: TransformableObject? = null

    override fun create() {
        super.create()
        background = Color4f.blue
        Camera.active.transform.position = Vector3f(0f, 0f, 4f)

        sceneScope.launch {
            val obj = Sphere()

            val pointLight = PointLight()
            pointLight.transform.position = Vector3f(1f, 1f, 1f)
            pointLight.color = Color4f.white
            pointLight.attenuation = 0.1f
            pointLight.intensity = 1.1f

            model = TransformableObject().apply {
                pointLight.parent = this
                addObject(this)
            }

            obj.transform.rotation = Quaternion.fromAxisAngle(Vector3f(0.5f, 1f, 0f), -45f)

            obj.material.setProperty("mainTexture", Loader.loadResource("textures/CubeTex.png"))
            obj.material.setProperty("pointLight", pointLight)
            obj.material.setProperty("reflectance", 32f)

            addObject(obj)
        }
    }

    override fun update() {
        super.update()
        model?.apply {
            transform.rotation *= Quaternion.fromAxisAngle(Vector3f.forward, 1f)
        }
    }
}