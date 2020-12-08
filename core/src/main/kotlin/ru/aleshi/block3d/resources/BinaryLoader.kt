package ru.aleshi.block3d.resources

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.lwjgl.system.MemoryUtil
import java.io.InputStream

/**
 * Loads resource to [java.nio.ByteBuffer]
 */
class BinaryLoader : IParser {

    override suspend fun parse(inputStream: InputStream): Any {
        return withContext(Dispatchers.IO) {
            val bytes = inputStream.use { it.readAllBytes() }
            val buffer = MemoryUtil.memAlloc(bytes.size + 1)
            buffer.put(bytes).rewind()
        }
    }

}