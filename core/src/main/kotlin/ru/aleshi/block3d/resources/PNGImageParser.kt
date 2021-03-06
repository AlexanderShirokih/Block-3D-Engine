package ru.aleshi.block3d.resources

import de.matthiasmann.twl.utils.PNGDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.Block3DException
import ru.aleshi.block3d.internal.data.Image2DData
import java.io.InputStream
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * A class used to load PNG images.
 * Loads Image as [Image2DData]
 * @see Image2DData
 */
class PNGImageParser : IParser {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun parse(inputStream: InputStream): Any {
        val decoder = withContext(Dispatchers.IO) { PNGDecoder(inputStream) }

        if (!decoder.isRGB)
            throw Block3DException("Cannot load image, because it's not an RGB")

        val hasAlpha = decoder.hasAlpha()
        val width = decoder.width
        val height = decoder.height
        val desiredFormat = if (hasAlpha) PNGDecoder.Format.RGBA else PNGDecoder.Format.RGB
        val bytesPerPixel = if (hasAlpha) 4 else 3
        val buffer = MemoryUtil.memAlloc(width * height * bytesPerPixel)

        try {
            withContext(Dispatchers.Default) { decoder.decode(buffer, width * bytesPerPixel, desiredFormat) }
        } catch (e: Exception) {
            MemoryUtil.memFree(buffer)
            throw e
        }

        return Image2DData(
            (buffer as Buffer).flip() as ByteBuffer,
            width,
            height,
            hasAlpha
        )
    }
}