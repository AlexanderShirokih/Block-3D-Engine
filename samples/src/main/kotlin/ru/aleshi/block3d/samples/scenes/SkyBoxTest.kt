package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Launcher
import ru.aleshi.block3d.World
import ru.aleshi.block3d.internal.data.ImageCubeData
import ru.aleshi.block3d.primitives.Box
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SkyBoxBackground
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f
import ru.aleshi.block3d.ui.ui

class SkyBoxTest : Scene() {

    override fun create() {
        super.create()
        Camera.active.transform.position = Vector3f(0f, 0f, 4f)

        add(Box())

        sceneScope.launch {
            background = SkyBoxBackground(Loader.loadResource("textures/cubemap/sorsele.cubemap") as ImageCubeData)
        }

        ui.setContent(
            createBackButton {
                World.current.launchSceneAsync(MainScene())
            }
        )
    }

    override fun update() {
        super.update()
        Camera.active.transform.rotate(Quaternion.fromAxisAngle(Vector3f.up, 1f))
    }
}
