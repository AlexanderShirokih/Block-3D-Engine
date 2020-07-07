package ru.aleshi.block3d.samples.scenes

import org.slf4j.LoggerFactory
import ru.aleshi.block3d.*
import ru.aleshi.block3d.data.ShaderData
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Matrix4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class DrawTriangle : Scene() {

    private val logger = LoggerFactory.getLogger(DrawTriangle::class.java)

    private val projection = Matrix4f()
    private val transform = Transform()

    override fun create() {
        super.create()

        logger.info("Scene created!")

        val shaderData = Loader.loadResource("shaders/plain_unlit.shc") as ShaderData

        // Create shader
        val shader = Shader(shaderData)

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
            )
        )

        val meshObject = MeshObject(Shared(mesh), Shared(shader))

        meshObject.bindings.apply {
            setProperty("projectionMatrix", projection)
            setProperty("worldMatrix", transform)
        }

        addObject(meshObject)

        transform.position = Vector3f(0f, 0f, -2f)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        projection.perspective(60f, width.toFloat() / height, 0.01f, 1000f)
    }

    override fun update() {
        super.update()
        transform.rotation *= Quaternion.fromAxisAngle(Vector3f(0f, 1f, 1f), 1f)
    }
}