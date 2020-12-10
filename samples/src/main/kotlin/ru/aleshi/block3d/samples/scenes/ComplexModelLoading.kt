package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Texture2D
import ru.aleshi.block3d.World
import ru.aleshi.block3d.internal.data.Image2DData
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.scenic.MeshObject
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f
import ru.aleshi.block3d.ui.ui

class ComplexModelLoading : Scene() {

    override fun create() {
        super.create()
        background = SolidColorBackground(Color4f.blue)

        sceneScope.launch {
            val islandTexture = Loader.loadResource("textures/diffuse.png") as Image2DData
            val islandModel = Loader.loadResource("models/island.obj")
            val monkeyModel = Loader.loadResource("models/monkey.obj")

            (islandModel as MeshObject).material.setProperty("mainTexture", Texture2D(islandTexture))
            islandModel.material.setProperty("ambientColor", Color4f.white)

            (monkeyModel as MeshObject).material.setProperty("ambientColor", Color4f.magenta)

            monkeyModel.transform.translate(Vector3f.up * 10f)
            monkeyModel.transform.scale(5f)

            add(islandModel)
            add(monkeyModel)

            Camera.active.transform.translate(Vector3f.forward * 40f)
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