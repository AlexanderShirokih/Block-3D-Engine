package ru.aleshi.block3d.samples.scenes

import ru.aleshi.block3d.*
import ru.aleshi.block3d.data.Image2DData
import ru.aleshi.block3d.data.ShaderData
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class DrawTriangle : Scene() {

    private lateinit var cube: MeshObject

    private fun buildMeshObject(): MeshObject {
        val shader = Shader(Loader.loadResource("shaders/plain_unlit.shc") as ShaderData)
        val main = Texture2D(Loader.loadResource("textures/box.png") as Image2DData)

        val material = Material(Shared(shader)).apply {
            setProperty("mainTexture", main)
        }

        // Load mesh
        val mesh = Mesh(
            floatArrayOf(
                // VO
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f
            ),
            intArrayOf(
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5
            ),
            floatArrayOf(
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
            )
        )

        return MeshObject(Shared(mesh), material)
    }

    override fun create() {
        super.create()
        Camera.active.transform.position = Vector3f(0f, 0f, 4f)

        cube = buildMeshObject()
        addObject(cube)

        cube.clone().apply {
            parent = cube
            transform.position = Vector3f(-1.5f, 0f, 0f)
        }
    }

    override fun update() {
        super.update()
        cube.transform.rotation *= Quaternion.fromAxisAngle(Vector3f(0f, 0f, 1f), 1f)
    }
}