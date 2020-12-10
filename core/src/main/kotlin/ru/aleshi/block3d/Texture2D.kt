package ru.aleshi.block3d

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import ru.aleshi.block3d.internal.data.Image2DData
import ru.aleshi.block3d.types.Color4f
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * A class representing texture 2D for binding it to the shader as sampler2D
 * @param imageData image source. After rewriting it to the memory [Image2DData.data] will be recycled
 */
class Texture2D(imageData: Image2DData) : Texture(GL_TEXTURE_2D) {

    init {
        if(imageData.isRecycled) throw Block3DException("Image2DData was already recycled!")
        val glFormat = if (imageData.hasAlpha) GL_RGBA else GL_RGB
        glBindTexture(glType, texId)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        glTexParameteri(
            glType,
            GL_TEXTURE_MIN_FILTER,
            if (imageData.generateMipmaps) GL_LINEAR_MIPMAP_LINEAR else GL_NEAREST
        )
        glTexParameteri(glType, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTexImage2D(
            glType,
            0,
            glFormat,
            imageData.width,
            imageData.height,
            0,
            glFormat,
            GL_UNSIGNED_BYTE,
            imageData.data
        )

        if (imageData.generateMipmaps)
            glGenerateMipmap(GL_TEXTURE_2D)

        imageData.recycle()
    }

    companion object {
        /**
         * Creates [Texture2D] with size 1x1 and filled with [color].
         */
        @JvmStatic
        fun ofColor(color: Color4f) = Texture2D(
            Image2DData(
                data = ByteBuffer
                    .allocateDirect(4)
                    .put((color.red * 0xFF).toInt().toByte())
                    .put((color.green * 0xFF).toInt().toByte())
                    .put((color.blue * 0xFF).toInt().toByte())
                    .put((color.alpha * 0xFF).toInt().toByte())
                    .apply { (this as Buffer).flip() } as ByteBuffer,
                width = 1,
                height = 1,
                hasAlpha = true,
                generateMipmaps = false
            )
        )
    }

}