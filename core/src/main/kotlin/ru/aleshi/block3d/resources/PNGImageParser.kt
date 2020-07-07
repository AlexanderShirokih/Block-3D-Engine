package ru.aleshi.block3d.resources

import de.matthiasmann.twl.utils.PNGDecoder
import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.internal.ImageData
import java.io.InputStream
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * A class used to load and save PNG images.
 * Loads Image as ImageData
 * @see ImageData
 */
class PNGImageParser : IParser {

    override fun parse(inputStream: InputStream): Any {
        val decoder = PNGDecoder(inputStream)

        if (!decoder.isRGB)
            throw RuntimeException("Cannot load image, because it's not an RGB")

        val hasAlpha = decoder.hasAlpha()
        val width = decoder.width
        val height = decoder.height
        val desiredFormat = if (hasAlpha) PNGDecoder.Format.RGBA else PNGDecoder.Format.RGB
        val bytesPerPixel = if (hasAlpha) 4 else 3
        val buffer = MemoryUtil.memAlloc(width * height * bytesPerPixel)

        try {
            decoder.decode(buffer, width * bytesPerPixel, desiredFormat)
        } catch (e: Exception) {
            MemoryUtil.memFree(buffer)
            throw e
        }

        return ImageData((buffer as Buffer).flip() as ByteBuffer, width, height, hasAlpha)
    }
}