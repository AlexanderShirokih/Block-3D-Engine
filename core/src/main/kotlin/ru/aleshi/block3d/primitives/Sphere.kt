package ru.aleshi.block3d.primitives

import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.*
import ru.aleshi.block3d.scenic.MeshObject
import ru.aleshi.block3d.types.AABB
import ru.aleshi.block3d.types.Vector3f
import java.nio.Buffer
import kotlin.math.cos
import kotlin.math.sin

/**
 * A sphere mesh object. By default uses [Defaults.MATERIAL_UNLIT] material.
 */
class Sphere : MeshObject {

    /**
     * Creates a new instance with the copy of the sphere mesh. Default sphere size: 20 slices x 20 stacks.
     */
    constructor(instanceMaterial: Material = Defaults.MATERIAL_LIT) : super(sharedMeshObject, instanceMaterial)

    /**
     * Creates a new instance with newly created sphere mesh sized by [slices] and [stacks].
     */
    constructor(slices: Int, stacks: Int, instanceMaterial: Material = Defaults.MATERIAL_LIT) : super(
        Shared(
            buildSphereMesh(slices, stacks)
        ), instanceMaterial
    )

    companion object {
        private const val SLICES = 20
        private const val STACKS = 20

        @JvmStatic
        private val SPHERE_BOUNDS = AABB(Vector3f(-1f), Vector3f.one)

        private val sharedMeshObject: Shared<Mesh> by lazy { Shared(buildSphereMesh(SLICES, STACKS)) }

        private fun buildSphereMesh(slices: Int, stacks: Int): Mesh {
            val vertexCount = (slices + 1) * (stacks + 1)
            val indexCount = 6 * slices * (stacks + 1)

            val dr = Math.PI / slices
            val ds = 2 * Math.PI / stacks

            val vertexBuffer = MemoryUtil.memAllocFloat(vertexCount * 5)
            val indexBuffer = MemoryUtil.memAllocShort(indexCount)

            var vertex = 0
            for (ring in 0..slices) {
                val r0 = sin(ring * dr)
                val y = cos(ring * dr).toFloat()
                for (seg in 0..stacks) {
                    val x = (r0 * sin(seg * ds)).toFloat()
                    val z = (r0 * cos(seg * ds)).toFloat()
                    vertexBuffer.put(x).put(y).put(z)
                        .put(seg.toFloat() / stacks).put(ring.toFloat() / slices)

                    if (ring != slices) {
                        val i0 = (vertex + stacks + 1).toShort()
                        val i1 = vertex.toShort()
                        val i2 = (vertex + stacks).toShort()
                        val i3 = (vertex + 1).toShort()
                        indexBuffer
                            .put(i0).put(i1).put(i2)
                            .put(i0).put(i3).put(i1)
                    }
                    vertex++
                }
            }

            (vertexBuffer as Buffer).flip()
            (indexBuffer as Buffer).flip()

            return Mesh.builder().apply {
                bounds(SPHERE_BOUNDS)
                vertices(vertexBuffer, 20)
                normals(vertexBuffer, 20)
                (vertexBuffer as Buffer).position(3)
                textureCoordinates(vertexBuffer, 20)
                indices(indexBuffer)

                MemoryUtil.memFree(vertexBuffer)
                MemoryUtil.memFree(indexBuffer)
            }.build()
        }
    }
}