package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.MeshObject
import ru.aleshi.block3d.Scene
import ru.aleshi.block3d.TransformableObject
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class TestScene : Scene() {

    private var model: TransformableObject? = null

    override fun create() {
        super.create()
        Camera.active.transform.position = Vector3f(0f, 0f, 6f)

        sceneScope.launch {
            val obj = Loader.loadResource("models/pyramid_and_cube.obj") as TransformableObject

            obj.forEach {
                it as MeshObject
                it.material.setProperty("color", Color4f.random)
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