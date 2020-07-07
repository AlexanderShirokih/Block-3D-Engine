package ru.aleshi.block3d

import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.Buffer

/**
 * A class describing 3D mesh objects
 * @param positions vertices array. Should have 3 float values per vertex
 * @param indices indices array
 */
class Mesh(positions: FloatArray, indices: IntArray) : IDisposable {

    private var arrayObjectId: Int = 0
    private var positionVboId: Int = 0
    private var indicesVboId: Int = 0

    /**
     * Vertex count in mesh
     */
    var vertexCount: Int = 0
        private set

    init {
        if (vertexCount != 0)
            dispose()

        vertexCount = indices.size

        arrayObjectId = glGenVertexArrays()
        glBindVertexArray(arrayObjectId)

        val verticesBuffer = MemoryUtil.memAllocFloat(positions.size)
        try {
            (verticesBuffer.put(positions) as Buffer).flip()

            positionVboId = glGenBuffers().apply {
                glBindBuffer(GL_ARRAY_BUFFER, this)
                glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
                glBindBuffer(GL_ARRAY_BUFFER, 0)
            }
        } finally {
            verticesBuffer.apply { MemoryUtil.memFree(this) }
        }

        val indicesBuffer = MemoryUtil.memAllocInt(indices.size)
        try {
            (indicesBuffer.put(indices) as Buffer).flip()
            indicesVboId = glGenBuffers().apply {
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this)
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
            }
        } finally {
            indicesBuffer.apply { MemoryUtil.memFree(this) }
        }

        glBindVertexArray(0)
    }

    fun draw() {
        // Draw the mesh
        glBindVertexArray(arrayObjectId)
        glEnableVertexAttribArray(0)

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        // Restore state
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }

    override fun dispose() {
        if (vertexCount == 0) return

        glDisableVertexAttribArray(0)

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(positionVboId)
        glDeleteBuffers(indicesVboId)

        // Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(arrayObjectId)

        vertexCount = 0
    }
}