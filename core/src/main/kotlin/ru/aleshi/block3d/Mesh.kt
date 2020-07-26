package ru.aleshi.block3d

import org.lwjgl.opengl.GL30C.*
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer


/**
 * A class describing 3D mesh objects. Contains id's of written to the memory geometry.
 */
class Mesh private constructor(
    private var arrayObjectId: Int,
    private val positionVboId: Int,
    private val normalsVboId: Int,
    private val texCoordsVboId: Int,
    private val indicesVboId: Int,
    private val glIndexType: Int
) : IDisposable {
    /**
     * Vertex count in mesh
     */
    var vertexCount: Int = 0
        private set

    class Builder {
        private val arrayObjectId = glGenVertexArrays()
        private var positionVboId: Int = BUFFER_NOT_SET
        private var normalsVboId: Int = BUFFER_NOT_SET
        private var texCoordsVboId: Int = BUFFER_NOT_SET
        private var indicesVboId: Int = BUFFER_NOT_SET
        private var glIndexType: Int = 0
        private var vertexCount: Int = 0
        private var indicesCount: Int = 0

        init {
            glBindVertexArray(arrayObjectId)
        }

        fun vertices(vertexBuffer: FloatBuffer, stride: Int = 0) {
            if (positionVboId != BUFFER_NOT_SET) throw RuntimeException("Vertices was already set")

            positionVboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, positionVboId)
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            vertexCount = vertexBuffer.capacity() / 3
        }

        fun normals(normalsBuffer: FloatBuffer, stride: Int = 0) {
            if (normalsVboId != BUFFER_NOT_SET) throw RuntimeException("Normals was already set")

            normalsVboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, normalsVboId)
            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
        }

        fun textureCoordinates(texCoordsBuffer: FloatBuffer, stride: Int = 0) {
            if (texCoordsVboId != BUFFER_NOT_SET) throw RuntimeException("Texture coordinates was already set")

            texCoordsVboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, texCoordsVboId)
            glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 0)
        }

        private fun createIndicesBuffer(buffer: Buffer) {
            if (indicesVboId != BUFFER_NOT_SET) throw RuntimeException("Indices was already set")

            indicesVboId = glGenBuffers()
            indicesCount = buffer.capacity()

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId)
        }

        fun indices(indicesBuffer: IntBuffer) {
            createIndicesBuffer(indicesBuffer)

            glIndexType = GL_UNSIGNED_INT
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
        }

        fun indices(indicesBuffer: ShortBuffer) {
            createIndicesBuffer(indicesBuffer)

            glIndexType = GL_UNSIGNED_SHORT
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
        }

        fun build(): Mesh {
            glBindVertexArray(0)
            return Mesh(
                arrayObjectId = arrayObjectId,
                positionVboId = positionVboId,
                normalsVboId = normalsVboId,
                texCoordsVboId = texCoordsVboId,
                indicesVboId = indicesVboId,
                glIndexType = glIndexType
            ).also { mesh ->
                if (indicesVboId == BUFFER_NOT_SET) {
                    mesh.dispose()
                    throw RuntimeException("Indices buffer was not specified")
                }
                mesh.vertexCount = if (indicesCount == 0) vertexCount else indicesCount
            }

        }
    }

    companion object {
        private const val BUFFER_NOT_SET = -1

        fun builder(): Builder = Builder()
    }

    fun draw(drawMode: Material.DrawMode) {
        glBindVertexArray(arrayObjectId)

        //TODO: Should be enabled by default
        glEnableVertexAttribArray(0)

        if (normalsVboId != BUFFER_NOT_SET)
            glEnableVertexAttribArray(1)
        if (texCoordsVboId != BUFFER_NOT_SET)
            glEnableVertexAttribArray(2)

        glDrawElements(drawMode.glValue, vertexCount, glIndexType, 0)

        // Restore state
        glDisableVertexAttribArray(0)

        if (normalsVboId != BUFFER_NOT_SET)
            glDisableVertexAttribArray(1)
        if (texCoordsVboId != BUFFER_NOT_SET)
            glDisableVertexAttribArray(2)

        glBindVertexArray(0)
    }

    override fun dispose() {
        if (arrayObjectId == 0) return

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(positionVboId)
        glDeleteBuffers(normalsVboId)
        glDeleteBuffers(texCoordsVboId)
        glDeleteBuffers(indicesVboId)

        // Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(arrayObjectId)

        arrayObjectId = 0
    }
}