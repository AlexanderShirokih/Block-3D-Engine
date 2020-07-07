package ru.aleshi.block3d.samples.scenes

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import ru.aleshi.block3d.*
import ru.aleshi.block3d.data.ShaderData
import ru.aleshi.block3d.internal.ImageData
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Matrix4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

class DrawTriangle : Scene() {

    private val projection = Matrix4f()
    private lateinit var cube: MeshObject

    var texId: Int = 0

    override fun create() {
        super.create()

        // Create shader
        val shader = Shader(Loader.loadResource("shaders/plain_unlit.shc") as ShaderData)
        val texture = Loader.loadResource("textures/box.png") as ImageData
        val glFormat = if (texture.hasAlpha) GL_RGBA else GL_RGB
        texId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texId)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            glFormat,
            texture.width,
            texture.height,
            0,
            glFormat,
            GL_UNSIGNED_BYTE,
            texture.data
        )

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

        cube = MeshObject(Shared(mesh), Shared(shader))
        cube.bindings.setProperty("projectionMatrix", projection)
        cube.bindings.setProperty("mainTexture", 0)
        cube.transform.position = Vector3f(0f, 0f, -2f)

        addObject(cube)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        projection.perspective(60f, width.toFloat() / height, 0.01f, 1000f)
    }

    override fun update() {
        GL13.glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texId)
        super.update()
        cube.transform.rotation *= Quaternion.fromAxisAngle(Vector3f(0f, 1f, 1f), 1f)
    }
}