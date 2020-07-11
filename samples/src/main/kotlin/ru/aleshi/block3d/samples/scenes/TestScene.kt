package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.*
import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.MeshObject
import ru.aleshi.block3d.Scene
import ru.aleshi.block3d.primitives.Sphere
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class TestScene : Scene() {

    private var model: MeshObject? = null

    override fun create() {
        super.create()
        Camera.active.transform.position = Vector3f(0f, 0f, 6f)

        sceneScope.launch {
            val sphere = Sphere()
            addObject(sphere)

            val sphere2 = Sphere()

            sphere2.parent = sphere
            sphere2.transform.position = Vector3f(-2.5f, 0f, 0f)

            model = sphere
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