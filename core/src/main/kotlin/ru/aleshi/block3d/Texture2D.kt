package ru.aleshi.block3d

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.data.Image2DData
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * A class representing texture 2D for binding it to the shader as sampler2D
 * @param imageData image source. After rewriting it to the memory [Image2DData.data] will be recycled
 */
class Texture2D(imageData: Image2DData) : Texture(GL_TEXTURE_2D) {

    companion object {

        val WHITE by lazy {
            Texture2D(
                Image2DData(
                    data = (ByteBuffer.allocateDirect(4).putInt(0xFFFFFFFF.toInt()) as Buffer).flip() as ByteBuffer,
                    width = 1,
                    height = 1,
                    hasAlpha = true,
                    generateMipmaps = false
                )
            )
        }

    }

    init {
        val glFormat = if (imageData.hasAlpha) GL_RGBA else GL_RGB
        glBindTexture(GL_TEXTURE_2D, texId)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MIN_FILTER,
            if (imageData.generateMipmaps) GL_LINEAR_MIPMAP_LINEAR else GL_NEAREST
        )
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTexImage2D(
            GL_TEXTURE_2D,
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

        MemoryUtil.memFree(imageData.data)
    }

}