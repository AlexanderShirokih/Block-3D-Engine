package ru.aleshi.block3d

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13C.*
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.internal.data.ImageCubeData
import ru.aleshi.block3d.types.Side

/**
 * A class representing texture Cube for binding it to the shader as samplerCube
 * @param imageData image source. After rewriting it to the memory [ImageCubeData.data] will be recycled
 */
class TextureCube(imageData: ImageCubeData) : Texture(GL_TEXTURE_CUBE_MAP) {

    init {
        GL11.glBindTexture(glType, texId)
        GL11.glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        GL11.glTexParameteri(
            glType, GL11.GL_TEXTURE_MIN_FILTER,
            if (imageData.generateMipmaps) GL_LINEAR_MIPMAP_LINEAR else GL_NEAREST
        )
        GL11.glTexParameteri(glType, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glTexParameteri(glType, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(glType, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(glType, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE)

        for ((side, data) in imageData.data) {
            val glFormat = if (data.hasAlpha) GL_RGBA else GL_RGB

            GL11.glTexImage2D(
                side.getGLType(),
                0,
                glFormat,
                data.width,
                data.height,
                0,
                glFormat,
                GL_UNSIGNED_BYTE,
                data.data
            )

            MemoryUtil.memFree(data.data)
        }


        if (imageData.generateMipmaps)
            GL30.glGenerateMipmap(glType)
    }

    private fun Side.getGLType() = GL_TEXTURE_CUBE_MAP_POSITIVE_X + ordinal

}