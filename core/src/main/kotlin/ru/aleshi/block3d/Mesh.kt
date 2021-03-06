package ru.aleshi.block3d

import org.lwjgl.opengl.GL30C.*
import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.types.AABB
import ru.aleshi.block3d.types.Vector3f
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer
import kotlin.math.*

/**
 * A class describing 3D mesh objects. Contains id's of written to the memory geometry.
 */
class Mesh private constructor(
    private var arrayObjectId: Int,
    private val positionVboId: Int,
    private val normalsVboId: Int,
    private val texCoordsVboId: Int,
    private val indicesVboId: Int,
    private val glIndexType: Int,
    val boundingBox: AABB
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
        private var aabb: AABB = AABB.Empty

        init {
            glBindVertexArray(arrayObjectId)
        }

        /**
         * Sets the bounds of object.
         * Should be set before [vertices] set. Otherwise bounds will be calculated automatically by vertices data.
         */
        fun bounds(aabb: AABB) {
            this.aabb = aabb
        }

        /**
         * Sets the vertices array
         */
        fun vertices(vertexArray: FloatArray) {
            val vBuffer = MemoryUtil.memAllocFloat(vertexArray.size).put(vertexArray)
            (vBuffer as Buffer).flip()
            vertices(vBuffer)
            MemoryUtil.memFree(vBuffer)
        }

        /**
         * Sets the vertices by buffer and stride
         */
        fun vertices(vertexBuffer: FloatBuffer, stride: Int = 0) {
            if (positionVboId != BUFFER_NOT_SET) throw Block3DException("Vertices was already set")

            positionVboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, positionVboId)
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            val s = if (stride == 0) 3 else stride / 4
            val buffCap = vertexBuffer.capacity()
            vertexCount = buffCap / s
            if (aabb == AABB.Empty)
                updateBounds(vertexBuffer, vertexCount, s)
        }

        private fun updateBounds(buffer: FloatBuffer, vCount: Int, stride: Int) {
            var mx = Float.MAX_VALUE
            var my = Float.MAX_VALUE
            var mz = Float.MAX_VALUE
            var mX = 0f
            var mY = 0f
            var mZ = 0f

            val b = buffer as Buffer
            val startPosition = b.position()
            repeat(vCount) {
                b.position(startPosition + it * stride)
                val x = buffer.get()
                val y = buffer.get()
                val z = buffer.get()

                mx = min(mx, x)
                mX = max(mX, x)
                my = min(my, y)
                mY = max(mY, y)
                mz = min(mz, z)
                mZ = max(mZ, z)
            }

            aabb = AABB(Vector3f(mx, my, mz), Vector3f(mX, mY, mZ))
        }

        fun normals(normalsBuffer: FloatBuffer, stride: Int = 0) {
            if (normalsVboId != BUFFER_NOT_SET) throw Block3DException("Normals was already set")

            normalsVboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, normalsVboId)
            glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
        }

        fun textureCoordinates(texCoordsBuffer: FloatBuffer, stride: Int = 0) {
            if (texCoordsVboId != BUFFER_NOT_SET) throw Block3DException("Texture coordinates was already set")

            texCoordsVboId = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, texCoordsVboId)
            glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW)
            glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 0)
        }

        private fun createIndicesBuffer(buffer: Buffer) {
            if (indicesVboId != BUFFER_NOT_SET) throw Block3DException("Indices was already set")

            indicesVboId = glGenBuffers()
            indicesCount = buffer.capacity()

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId)
        }

        fun indices(indicesArray: ShortArray) {
            val iBuffer = MemoryUtil.memAllocShort(indicesArray.size).put(indicesArray)
            (iBuffer as Buffer).flip()
            indices(iBuffer)
            MemoryUtil.memFree(iBuffer)
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
                glIndexType = glIndexType,
                boundingBox = aabb
            ).also { mesh ->
                if (indicesVboId == BUFFER_NOT_SET) {
                    mesh.dispose()
                    throw MeshRenderingException("Indices buffer was not specified")
                }
                if (positionVboId == BUFFER_NOT_SET) {
                    mesh.dispose()
                    throw MeshRenderingException("Vertex buffer was not specified")
                }
                mesh.vertexCount = if (indicesCount == 0) vertexCount else indicesCount
            }

        }
    }

    companion object {
        private const val BUFFER_NOT_SET = -1

        fun builder(): Builder = Builder()
    }

    /**
     * Sets the current vertex buffer data
     */
    fun setVertices(buffer: FloatBuffer) {
        glBindBuffer(GL_ARRAY_BUFFER, positionVboId)
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    /**
     * Binds vertex arrays
     */
    fun bind() {
        glBindVertexArray(arrayObjectId)

        //TODO: Should be enabled by default
        glEnableVertexAttribArray(0)

        if (normalsVboId != BUFFER_NOT_SET)
            glEnableVertexAttribArray(1)
        if (texCoordsVboId != BUFFER_NOT_SET)
            glEnableVertexAttribArray(2)
    }

    /**
     * Resets vertex binding
     */
    fun unbind() {
        // Restore state
        glDisableVertexAttribArray(0)

        if (normalsVboId != BUFFER_NOT_SET)
            glDisableVertexAttribArray(1)
        if (texCoordsVboId != BUFFER_NOT_SET)
            glDisableVertexAttribArray(2)

        glBindVertexArray(0)
    }

    /**
     * Draws the mesh
     */
    fun draw() {
        glDrawElements(GL_TRIANGLES, vertexCount, glIndexType, 0)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mesh

        if (arrayObjectId != other.arrayObjectId) return false
        if (positionVboId != other.positionVboId) return false
        if (normalsVboId != other.normalsVboId) return false
        if (texCoordsVboId != other.texCoordsVboId) return false
        if (indicesVboId != other.indicesVboId) return false

        return true
    }

    override fun hashCode(): Int = arrayObjectId
}