package ru.aleshi.block3d.resources

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.*
import java.io.BufferedReader
import java.io.InputStream
import java.nio.Buffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

/**
 * Loads Wavefront models (.OBJ)
 */
class WavefrontObjectParser : IParser {

    private fun MutableList<String>.getIndexAndPut(face: String): Int {
        val i = this.indexOf(face)
        if (i == -1) {
            this.add(face)
            return this.size - 1
        }
        return i
    }

    private class RemappedFaceList(
        val indexBuffer: Buffer,
        val indexMappings: List<String>,
        val isShortBuffer: Boolean
    )

    override suspend fun parse(inputStream: InputStream): Any =
        inputStream.bufferedReader().use { reader ->
            val groups = withContext(Dispatchers.Default) { parseGroupedObjects(reader) }

            val vertexList = groups.filter { it.first == "v" }.map { it.second as List<FloatArray> }
            val normalList = groups.filter { it.first == "vn" }.map { it.second as List<FloatArray> }
            val texCoordsList = groups.filter { it.first == "vt" }.map { it.second as List<FloatArray> }
            val facesList = groups.filter { it.first == "f" }.map(::remapIndices)

            val allDataPresent =
                listOf(vertexList, normalList, texCoordsList, facesList).all { it.size == vertexList.size }

            if (!allDataPresent)
                throw RuntimeException("Not all data present. Vertex({$vertexList.size}), normals({$normalList.size}), texture coordinates({$texCoordsList.size}) and faces({$facesList.size}) groups count not matches")

            val numObjects = facesList.size
            var vertexOffset = 1
            var normalsOffset = 1
            var texCoordsOffset = 1

            var root: TransformableObject? = null


            facesList.forEachIndexed { index, remappedFaceList ->
                val buffersSize = remappedFaceList.indexMappings.size * 3
                val vertexBuffer = MemoryUtil.memAllocFloat(buffersSize)
                val normalsBuffer = MemoryUtil.memAllocFloat(buffersSize)
                val texCoordsBuffer = MemoryUtil.memAllocFloat(remappedFaceList.indexMappings.size * 2)

                withContext(Dispatchers.Default) {
                    val vertexes = vertexList[index]
                    val normals = normalList[index]
                    val texCoords = texCoordsList[index]

                    for (indexGroup in remappedFaceList.indexMappings) {
                        val (v, t, n) = indexGroup.split('/')

                        vertexBuffer.put(vertexes[v.toInt() - vertexOffset])
                        normalsBuffer.put(normals[n.toInt() - normalsOffset])
                        texCoordsBuffer.put(texCoords[t.toInt() - texCoordsOffset])
                    }

                    vertexOffset += vertexes.size
                    normalsOffset += normals.size
                    texCoordsOffset += texCoords.size

                    (vertexBuffer as Buffer).flip()
                    (normalsBuffer as Buffer).flip()
                    (texCoordsBuffer as Buffer).flip()
                }

                val mesh = Mesh.builder().apply {
                    vertices(vertexBuffer, 0)
                    normals(normalsBuffer, 0)
                    textureCoordinates(texCoordsBuffer, 0)

                    if (remappedFaceList.isShortBuffer)
                        indices(remappedFaceList.indexBuffer as ShortBuffer)
                    else
                        indices(remappedFaceList.indexBuffer as IntBuffer)

                }.build()

                MemoryUtil.memFree(remappedFaceList.indexBuffer)
                MemoryUtil.memFree(vertexBuffer)
                MemoryUtil.memFree(normalsBuffer)
                MemoryUtil.memFree(texCoordsBuffer)

                val meshObject = MeshObject(Shared(mesh), Defaults.MATERIAL_UNLIT)
                if (numObjects == 1)
                    root = meshObject
                else {
                    if (root == null)
                        root = TransformableObject()
                    meshObject.parent = root
                }
            }
            return root ?: throw RuntimeException("Parsed model doesn't have any mesh in it")
        }

    private fun remapIndices(pair: Pair<String, MutableList<Any>>): RemappedFaceList {
        val indicesStrings = pair.second as List<List<String>>

        val mappingGroup = ArrayList<String>(indicesStrings.size)
        val isShortBuffer = indicesStrings.size <= Short.MAX_VALUE * 2

        val buffer: Buffer = if (isShortBuffer) {
            val shortBuffer = MemoryUtil.memAllocShort(indicesStrings.size * 3)
            for ((i0, i1, i2) in indicesStrings) {
                shortBuffer
                    .put(mappingGroup.getIndexAndPut(i0).toShort())
                    .put(mappingGroup.getIndexAndPut(i1).toShort())
                    .put(mappingGroup.getIndexAndPut(i2).toShort())
            }
            shortBuffer
        } else {
            val intBuffer = MemoryUtil.memAllocInt(indicesStrings.size * 3)
            for ((i0, i1, i2) in indicesStrings) {
                intBuffer
                    .put(mappingGroup.getIndexAndPut(i0))
                    .put(mappingGroup.getIndexAndPut(i1))
                    .put(mappingGroup.getIndexAndPut(i2))
            }
            intBuffer
        }

        buffer.flip()

        return RemappedFaceList(
            indexBuffer = buffer,
            indexMappings = mappingGroup,
            isShortBuffer = isShortBuffer
        )
    }

    private fun parseGroupedObjects(reader: BufferedReader) =
        reader
            .lineSequence()
            .filter { it.isNotEmpty() && !it.startsWith('#') }
            .map { it.split(' ') }
            .map {
                when (it[0]) {
                    "v", "vn" -> it[0] to floatArrayOf(it[1].toFloat(), it[2].toFloat(), it[3].toFloat())
                    "vt" -> it[0] to floatArrayOf(it[1].toFloat(), it[2].toFloat())
                    "f" -> if (it.size == 4) {
                        it[0] to listOf(it[1], it[2], it[3])
                    } else throw RuntimeException("Only triangle faces supported")
                    "o", "g", "mtllib", "usemtl" -> it[0] to it[1]
                    else -> null
                }
            }
            .filterNotNull()
            .fold(mutableListOf<Pair<String, MutableList<Any>>>()) { sum, c ->
                if (sum.isEmpty() || sum.last().first != c.first) {
                    sum.add(c.first to mutableListOf(c.second))
                } else {
                    sum.last().second += c.second
                }
                sum
            }
}