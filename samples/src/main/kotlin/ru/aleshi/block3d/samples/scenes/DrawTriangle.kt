package ru.aleshi.block3d.samples.scenes

import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.MeshObject
import ru.aleshi.block3d.Scene
import ru.aleshi.block3d.Texture2D
import ru.aleshi.block3d.data.Image2DData
import ru.aleshi.block3d.primitives.Box
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class DrawTriangle : Scene() {

    private lateinit var cube: MeshObject

    override fun create() {
        super.create()
        Camera.active.transform.position = Vector3f(0f, 0f, 6f)

        cube = Box().apply {
            material.setProperty("mainTexture", Texture2D(Loader.loadResource("textures/box.png") as Image2DData))

            clone().also { secondCube ->
                secondCube.parent = this
                secondCube.transform.position = Vector3f(-2.5f, 0f, 0f)
            }
            addObject(this)
        }
    }

    override fun update() {
        super.update()
        cube.transform.rotation *= Quaternion.fromAxisAngle(
            Vector3f(0f, 1f, 1f), 1f
        )
    }
}