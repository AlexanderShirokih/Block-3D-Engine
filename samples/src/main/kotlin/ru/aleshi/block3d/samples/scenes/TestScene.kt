package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.MeshObject
import ru.aleshi.block3d.Scene
import ru.aleshi.block3d.TransformableObject
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class TestScene : Scene() {

    private var model: TransformableObject? = null

    override fun create() {
        super.create()
        Camera.active.transform.position = Vector3f(0f, 0f, 6f)

        sceneScope.launch {
            val obj = Loader.loadResource("models/cube.obj") as TransformableObject
            obj as MeshObject

            obj.material.setProperty("mainTexture", Loader.loadResource("textures/CubeTex.png"))

            obj.clone().also { clone ->
                clone.parent = obj
                clone.transform.position = Vector3f(-4f, 0f, 0f)
            }

            addObject(obj)
            model = obj
        }
    }

    override fun update() {
        super.update()

        model?.apply {
            transform.rotation *= Quaternion.fromAxisAngle(
                Vector3f(0f, 1f, 1f), 1f
            )
        }
    }
}