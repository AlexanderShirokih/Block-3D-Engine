package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.*
import ru.aleshi.block3d.Defaults
import ru.aleshi.block3d.World
import ru.aleshi.block3d.debug.FPSCounter
import ru.aleshi.block3d.primitives.Box
import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f
import ru.aleshi.block3d.ui.ui

class ObjectBatching : Scene() {

    private val counter = FPSCounter()

    override fun create() {
        super.create()
        background = SolidColorBackground(Color4f.blue)
        Camera.active.transform.position = (Vector3f.forward * 40f)

        repeat(10000) {
            add(Box(Defaults.MATERIAL_UNLIT).apply {
                transform.translate(Vector3f.random * 200f - Vector3f.one * 100f)
                transform.rotate(Quaternion.random)
                material.setProperty("color", Color4f.random)
            })
        }

        sceneScope.launch {
            while (isActive) {
                withContext(Dispatchers.Default) {
                    delay(1000L)
                }
                println("FPS=${counter.fps}")
            }
        }

        ui.setContent(
            createBackButton {
                World.current.launchSceneAsync(MainScene())
            }
        )
    }

    override fun update() {
        objects
            .asSequence()
            .filterIsInstance(Box::class.java)
            .forEach { it.transform.rotate(Quaternion.random) }

        super.update()
        counter.update()
    }

}