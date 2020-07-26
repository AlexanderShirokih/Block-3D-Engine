package ru.aleshi.block3d.samples.scenes

import kotlinx.coroutines.launch
import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.Scene
import ru.aleshi.block3d.TransformableObject
import ru.aleshi.block3d.debug.DebugRenderer
import ru.aleshi.block3d.debug.Line
import ru.aleshi.block3d.lights.DirectionalLight
import ru.aleshi.block3d.primitives.Box
import ru.aleshi.block3d.primitives.Plane
import ru.aleshi.block3d.primitives.Sphere
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class TestScene : Scene() {

    private val root = TransformableObject()

    override fun create() {
        setRenderer(DebugRenderer().apply {
            drawMode = DebugRenderer.DrawMode.WIREFRAME
        })

        super.create()
        background = Color4f.blue
        Camera.active.transform.position = Vector3f(0f, 0f, 20f)
        Camera.active.transform.rotation = Quaternion.fromAxisAngle(Vector3f(0.5f, 0f, 0f), -45f)

        sceneScope.launch {
            val pointLight = DirectionalLight()
            pointLight.transform.position = Vector3f(5f, 1f)

            addObject(root)
            addObject(pointLight, root)
            addObject(Sphere(), pointLight)

            val obj = Plane()
            obj.transform.scale = Vector3f.one * 20f
            obj.material.setProperty("mainTexture", Loader.loadResource("textures/CubeTex.png"))
            obj.material.setProperty("lightSource", pointLight)

            addObject(obj, root)

            repeat(20) {
                val cube = Box()
                cube.transform.rotation = Quaternion.random
                cube.transform.position = Vector3f.one * -7f + Vector3f.random * 14f
                Line.draw(cube.transform.position, Vector3f.up * 5f, Color4f.random, true)
                addObject(cube)
            }
        }
    }

    override fun update() {
        super.update()
        root.transform.position
        root.transform.rotation *= Quaternion.fromAxisAngle(Vector3f.up, 1f)
    }
}