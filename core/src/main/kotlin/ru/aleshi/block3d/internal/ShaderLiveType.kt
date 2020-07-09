package ru.aleshi.block3d.internal

import org.lwjgl.opengl.*
import ru.aleshi.block3d.*
import ru.aleshi.block3d.data.ShaderData
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Matrix4f
import java.nio.Buffer
import java.nio.FloatBuffer

internal sealed class ShaderLiveType(val uniformId: Int) {

    companion object LiveTypeDefaults {
        fun fromType(type: ShaderData.Property.Type, uniformLocation: Int): ShaderLiveType =
            when (type) {
                ShaderData.Property.Type.Float -> TODO()
                ShaderData.Property.Type.Color -> ColorLiveType(uniformLocation)
                ShaderData.Property.Type.Texture2D -> TextureLiveType(uniformLocation)
                ShaderData.Property.Type.Vector4 -> TODO()
                ShaderData.Property.Type.Matrix4 -> MatrixLiveType(uniformLocation)
            }

        val DEFAULT_MATRIX = Matrix4f()

    }

    abstract fun bind(buffer: Buffer)

    abstract fun set(value: Any?)

    abstract fun get(): Any

    /**
     * Applies [Matrix4f] or [Transform] or [()->Matrix4f] to mat4 uniform
     */
    class MatrixLiveType(uniformId: Int) : ShaderLiveType(uniformId) {
        private var matrixProvider: () -> Matrix4f = { DEFAULT_MATRIX }

        override fun bind(buffer: Buffer) {
            GL20C.glUniformMatrix4fv(uniformId, false, matrixProvider().store(buffer as FloatBuffer))
        }

        @Suppress("UNCHECKED_CAST")
        override fun set(value: Any?) = when (value) {
            null -> matrixProvider = { DEFAULT_MATRIX }
            is Matrix4f -> matrixProvider = { value }
            is Transform -> matrixProvider = value::matrix
            is Function<*> -> matrixProvider = value as () -> Matrix4f
            else ->
                throw ShaderException(
                    ShaderException.ErrorType.PropertyTypeMismatch,
                    "Cannot cast ${value.javaClass} to Matrix4f or Transform"
                )
        }

        override fun get() = matrixProvider

    }

    /**
     * Applies [Texture] to sampler uniform
     */
    class TextureLiveType(uniformId: Int) : ShaderLiveType(uniformId) {

        private var texture: Texture = Defaults.TEXTURE_WHITE

        var slot: Int = 0
            internal set

        override fun bind(buffer: Buffer) {
            GL13C.glActiveTexture(GL13.GL_TEXTURE0 + slot)
            GL11C.glBindTexture(texture.glType, texture.texId)
            GL20C.glUniform1i(uniformId, slot)
        }

        override fun set(value: Any?) {
            if (value == null) {
                texture = Defaults.TEXTURE_WHITE
                return
            } else
                if (value !is Texture)
                    throw ShaderException(
                        ShaderException.ErrorType.PropertyTypeMismatch,
                        "Cannot cast ${value.javaClass} to Texture"
                    )
            texture = value
        }

        override fun get() = texture
    }

    /**
     * Applies [Color4f] to vec4 uniform
     */
    class ColorLiveType(uniformId: Int) : ShaderLiveType(uniformId) {

        private var color: Color4f = Color4f.white

        override fun bind(buffer: Buffer) {
            GL20C.glUniform4f(uniformId, color.red, color.green, color.blue, color.alpha)
        }

        override fun set(value: Any?) {
            color =
                when (value) {
                    null -> Color4f.white
                    is Color4f -> value
                    else -> throw ShaderException(
                        ShaderException.ErrorType.PropertyTypeMismatch,
                        "Cannot cast ${value.javaClass} to Color4f"
                    )
                }
        }

        override fun get() = color

    }

}