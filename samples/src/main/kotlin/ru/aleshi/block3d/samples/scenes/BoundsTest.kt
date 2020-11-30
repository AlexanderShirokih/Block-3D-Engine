package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.*
import ru.aleshi.block3d.Defaults
import ru.aleshi.block3d.Launcher
import ru.aleshi.block3d.debug.DebugRenderer
import ru.aleshi.block3d.debug.Line
import ru.aleshi.block3d.internal.WindowConfig
import ru.aleshi.block3d.primitives.Box
import ru.aleshi.block3d.primitives.Sphere
import ru.aleshi.block3d.renderer.Frustum
import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.scenic.MeshObject
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

fun main() {
    Launcher.start(BoundsTest(), WindowConfig(vSync = false))
}

class BoundsTest : Scene() {

    private val frustum = Frustum()

    override fun create() {
        setRenderer(DebugRenderer())
        super.create()
        (background as SolidColorBackground).color = Color4f.blue
        Camera.active.transform.position = (Vector3f.forward * 10f)

        repeat(20) {
            add((if (it % 2 == 0) Sphere(Defaults.MATERIAL_UNLIT) else Box(Defaults.MATERIAL_UNLIT)).apply {
                transform.translate(Vector3f.random * 10f - Vector3f.one * 5f)
                transform.rotate(Quaternion.random)
                material.setProperty("color", Color4f.random)

                Line.draw(
                    transform.position + sharedMesh.getAndInc().boundingBox.min,
                    sharedMesh.getAndInc().boundingBox.size,
                    keepInScene = true
                )
            })
        }

        sceneScope.launch {
            while (isActive) {
                withContext(Dispatchers.Default) { delay(1000) }

                frustum.updateFrustum()

                var drawn = 0

                for (o in objects) {
                    if (o is MeshObject) {
                        o.sharedMesh.apply {
                            val m = getAndInc()

                            if (frustum.boxInFrustum(m.boundingBox, o.transform.position))
//                            if (frustum.pointInFrustum(o.transform.position))
                            {
                                drawn++
//
                                o.material.setProperty("color", Color4f.red)
                            } else
                                o.material.setProperty("color", Color4f.green)

                            decRef()
                        }
                    }
                }

                println("Drawn $drawn/${objects.size} objects")
            }
        }
    }

}