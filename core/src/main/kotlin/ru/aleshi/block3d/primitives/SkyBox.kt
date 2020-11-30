package ru.aleshi.block3d.primitives

import ru.aleshi.block3d.Defaults
import ru.aleshi.block3d.Material
import ru.aleshi.block3d.Mesh
import ru.aleshi.block3d.Shared
import ru.aleshi.block3d.scenic.MeshObject
import ru.aleshi.block3d.types.AABB
import ru.aleshi.block3d.types.Vector3f

/**
 * Skybox is an inside-out box. Usually used to draw scene background.
 */
class SkyBox(instanceMaterial: Material = Defaults.MATERIAL_SKYBOX) : MeshObject(skyBoxMesh, instanceMaterial) {

    companion object {
        @JvmStatic
        private val SKYBOX_BOUNDS = AABB(Vector3f(-1f), Vector3f.one)

        private val skyBoxMesh: Shared<Mesh> by lazy { Shared(generateMesh()) }

        private fun generateMesh(): Mesh {
            val meshBuilder = Mesh.builder()
            meshBuilder.bounds(SKYBOX_BOUNDS)

            meshBuilder.vertices(
                floatArrayOf(
                    // Front
                    -1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,
                    // Right
                    1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, -1.0f,
                    1.0f, 1.0f, 1.0f,
                    // Back
                    1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,
                    1.0f, 1.0f, -1.0f,
                    //Left
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f, -1.0f,
                    //Top
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,
                    // Bottom
                    -1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f
                )
            )

            meshBuilder.indices(
                shortArrayOf(
                    2, 1, 0,
                    0, 3, 2,
                    6, 5, 4,
                    4, 7, 6,
                    10, 9, 8,
                    8, 11, 10,
                    14, 13, 12,
                    12, 15, 14,
                    18, 17, 16,
                    16, 19, 18,
                    22, 21, 20,
                    20, 23, 22
                )
            )

            return meshBuilder.build()
        }
    }
}