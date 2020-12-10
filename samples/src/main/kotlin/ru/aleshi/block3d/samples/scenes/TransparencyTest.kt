package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Texture2D
import ru.aleshi.block3d.World
import ru.aleshi.block3d.internal.data.Image2DData
import ru.aleshi.block3d.primitives.Box
import ru.aleshi.block3d.primitives.Plane
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.scenic.TransformableObject
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f
import ru.aleshi.block3d.ui.ui

class TransparencyTest : Scene() {

    private val root = TransformableObject()

    override fun create() {

        super.create()
        background = SolidColorBackground(Color4f.blue)

        Camera.active.transform.position = Vector3f(0f, 0f, 5f)
        Camera.active.transform.rotation = Quaternion.fromAxisAngle(Vector3f(0.5f, 0f, 0f), -45f)

        blending = true
        add(root)

        sceneScope.launch {
            val boxTexture = Loader.loadResource("textures/box.png") as Image2DData
            val texture = Loader.loadResource("textures/glass.png") as Image2DData

            val plane = Plane()
            plane.transform.translate(Vector3f.forward * -4f)
            plane.transform.rotate(Quaternion.fromAxisAngle(Vector3f.right, -90f))
            plane.transform.scale(5f)

            plane.material.setProperty("mainTexture", Texture2D(boxTexture))
            plane.material.setProperty("ambientColor", Color4f.white)
            add(plane)

            val cube = Box()
            cube.transform.scale(2f)
            cube.material.setProperty("mainTexture", Texture2D(texture))
            cube.material.setProperty("ambientColor", Color4f.white)
            add(cube, root)
        }

        ui.setContent(
            createBackButton {
                World.current.launchSceneAsync(MainScene())
            }
        )
    }

    override fun update() {
        super.update()
        root.transform.rotate(Quaternion.fromAxisAngle(Vector3f.up, 1f))
    }
}