package ru.aleshi.block3d.primitives

import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.*
import ru.aleshi.block3d.scenic.MeshObject
import ru.aleshi.block3d.types.AABB
import ru.aleshi.block3d.types.Vector3f
import java.nio.Buffer

/**
 * A single-sided plane, lying on the x-z axis.
 * @constructor creates a new object with shared plane mesh and [Defaults.MATERIAL_LIT] material.
 */
class Plane(instanceMaterial: Material = Defaults.MATERIAL_LIT) : MeshObject(sharedPlaneMesh, instanceMaterial) {

    companion object {
        @JvmStatic
        private val PLANE_BOUNDS = AABB(Vector3f(-0.5f, 0.00001f, -0.5f), Vector3f(0.5f, 0.00001f, 0.5f))

        val sharedPlaneMesh: Shared<Mesh> by lazy { Shared(buildMesh()) }

        private fun buildMesh(): Mesh {
            val vertices = floatArrayOf(
                -0.5f, 0.0f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                0.5f, 0.0f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.0f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, 0.0f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f
            )
            val indices = shortArrayOf(
                0, 1, 2,
                2, 3, 0
            )

            val vBuffer = MemoryUtil.memAllocFloat(vertices.size).put(vertices)
            val iBuffer = MemoryUtil.memAllocShort(indices.size).put(indices)

            (vBuffer as Buffer).flip()
            (iBuffer as Buffer).flip()

            val mesh = Mesh.builder().apply {
                bounds(PLANE_BOUNDS)
                vertices(vBuffer, 32)
                (vBuffer as Buffer).position(3)
                normals(vBuffer, 32)
                (vBuffer as Buffer).position(6)
                textureCoordinates(vBuffer, 32)
                indices(iBuffer)
            }.build()

            MemoryUtil.memFree(vBuffer)
            MemoryUtil.memFree(iBuffer)

            return mesh
        }
    }
}