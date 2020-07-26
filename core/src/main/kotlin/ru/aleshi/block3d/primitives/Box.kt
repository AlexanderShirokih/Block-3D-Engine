package ru.aleshi.block3d.primitives

import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.*
import java.nio.Buffer

/**
 * A box mesh object.
 * @constructor creates a new object with shared box mesh and [Defaults.MATERIAL_LIT] material.
 */
class Box(instanceMaterial: Material = Defaults.MATERIAL_LIT) : MeshObject(sharedBoxMesh, instanceMaterial) {

    companion object {
        private val sharedBoxMesh: Shared<Mesh> by lazy { Shared(generateMesh()) }

        private fun generateMesh(): Mesh {
            val meshBuilder = Mesh.builder()

            val positions = floatArrayOf(
                // Front
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                // Right
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                // Back
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                //Left
                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                //Top
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                // Bottom
                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f
            )

            val buffer = MemoryUtil.memAllocFloat(positions.size)
            (buffer.put(positions) as Buffer).flip()

            meshBuilder.vertices(buffer, 32)

            (buffer as Buffer).position(6)
            meshBuilder.textureCoordinates(buffer, 32)

            (buffer as Buffer).position(3)
            meshBuilder.normals(buffer, 32)

            MemoryUtil.memFree(buffer)

            val indices = shortArrayOf(
                0, 1, 2,
                2, 3, 0,
                4, 5, 6,
                6, 7, 4,
                8, 9, 10,
                10, 11, 8,
                12, 13, 14,
                14, 15, 12,
                16, 17, 18,
                18, 19, 16,
                20, 21, 22,
                22, 23, 20
            )
            MemoryUtil.memFree(
                MemoryUtil.memAllocShort(indices.size).apply {
                    (put(indices) as Buffer).flip()
                    meshBuilder.indices(this)
                }
            )

            return meshBuilder.build()
        }
    }
}