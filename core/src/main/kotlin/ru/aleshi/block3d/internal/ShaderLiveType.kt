package ru.aleshi.block3d.internal

import org.lwjgl.opengl.GL20
import ru.aleshi.block3d.ShaderException
import ru.aleshi.block3d.Transform
import ru.aleshi.block3d.data.ShaderData
import ru.aleshi.block3d.types.Matrix4f
import java.nio.Buffer
import java.nio.FloatBuffer

internal sealed class ShaderLiveType(val uniformId: Int) {

    companion object LiveTypeDefaults {
        fun fromType(type: ShaderData.Property.Type, uniformLocation: Int): ShaderLiveType =
            when (type) {
                ShaderData.Property.Type.Float -> TODO()
                ShaderData.Property.Type.Color -> TODO()
                ShaderData.Property.Type.Texture2D -> TODO()
                ShaderData.Property.Type.Vector4 -> TODO()
                ShaderData.Property.Type.Matrix4 -> MatrixLiveType(uniformLocation)
            }

        val DEFAULT_MATRIX = Matrix4f()

    }

    abstract fun bind(buffer: Buffer)

    abstract fun set(value: Any)

    /**
     * Applies Matrix4f to mat4 uniform
     */
    class MatrixLiveType(uniformId: Int) : ShaderLiveType(uniformId) {
        private var matrixProvider: () -> Matrix4f = { DEFAULT_MATRIX }

        override fun bind(buffer: Buffer) {
            GL20.glUniformMatrix4fv(uniformId, false, matrixProvider().store(buffer as FloatBuffer))
        }

        override fun set(value: Any) = when (value) {
            is Matrix4f -> matrixProvider = { value }
            is Transform -> matrixProvider = value::matrix
            else ->
                throw ShaderException(
                    ShaderException.ErrorType.PropertyTypeMismatch,
                    "Cannot cast ${value.javaClass} to Matrix4f or Transform"
                )
        }

    }

}