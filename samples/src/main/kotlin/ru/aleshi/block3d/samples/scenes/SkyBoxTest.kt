package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Launcher
import ru.aleshi.block3d.internal.data.ImageCubeData
import ru.aleshi.block3d.primitives.Box
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SkyBoxBackground
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

fun main() {
    Launcher.start(SkyBoxTest())
}

class SkyBoxTest : Scene() {

    private lateinit var mainCamera: Camera

    override fun create() {
        super.create()
        mainCamera = Camera.active
        Camera.active.transform.position = Vector3f(0f, 0f, 4f)

        sceneScope.launch {
            background =
                SkyBoxBackground(Loader.loadResource("textures/cubemap/sorsele/sorsele.cubemap.json") as ImageCubeData)

            add(Box())
        }
    }

    override fun update() {
        super.update()
        Camera.active.transform.rotation *= Quaternion.fromAxisAngle(Vector3f.up, 1f)
    }
}
