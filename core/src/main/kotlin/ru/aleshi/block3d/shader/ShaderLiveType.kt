package ru.aleshi.block3d.shader

import ru.aleshi.block3d.data.ShaderData.Property.Type
import ru.aleshi.block3d.shader.Shader.ShaderProperty
import org.lwjgl.opengl.GL11C
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL13C
import org.lwjgl.opengl.GL20C
import ru.aleshi.block3d.*
import ru.aleshi.block3d.lights.DirectionalLight
import ru.aleshi.block3d.lights.LightSource
import ru.aleshi.block3d.lights.PointLight
import ru.aleshi.block3d.shader.Shader.ShaderProperty.SingleShaderProperty
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Matrix4f
import ru.aleshi.block3d.types.Vector3f
import java.nio.Buffer
import java.nio.FloatBuffer

internal sealed class ShaderLiveType {

    companion object {
        fun fromType(shaderProperty: ShaderProperty): ShaderLiveType =
            when (shaderProperty.type) {
                Type.Float -> FloatLiveType(shaderProperty)
                Type.ColorRGB -> ColorLiveType(false, shaderProperty)
                Type.ColorRGBA -> ColorLiveType(true, shaderProperty)
                Type.Texture2D -> TextureLiveType(shaderProperty)
                Type.Vector3 -> Vector3LiveType(shaderProperty)
                Type.Matrix4 -> MatrixLiveType(shaderProperty)
                Type.LightSource -> LightSourceLiveType(shaderProperty)
            }

        val DEFAULT_MATRIX = Matrix4f()

        fun createTypeCastException(current: Any, desired: List<Class<*>>) =
            ShaderException(
                ShaderException.ErrorType.PropertyTypeMismatch,
                "Cannot cast ${current.javaClass} to ${desired.joinToString { it.simpleName }}"
            )

    }

    abstract fun bind(buffer: Buffer)

    abstract fun set(value: Any?)

    abstract fun get(): Any?

    /**
     * Applies [Matrix4f] or [Transform] or [()->Matrix4f] to mat4 uniform
     */
    class MatrixLiveType(shaderProperty: ShaderProperty) : ShaderLiveType() {
        private val uniformId: Int = (shaderProperty as SingleShaderProperty).uniformId
        private var matrixProvider: () -> Matrix4f = { DEFAULT_MATRIX }

        override fun bind(buffer: Buffer) {
            GL20C.glUniformMatrix4fv(uniformId, false, matrixProvider().store(buffer as FloatBuffer))
        }

        @Suppress("UNCHECKED_CAST")
        override fun set(value: Any?) {
            when (value) {
                null -> matrixProvider = { DEFAULT_MATRIX }
                is Matrix4f -> matrixProvider = { value }
                is Transform -> matrixProvider = value::matrix
                is Function<*> -> matrixProvider = value as () -> Matrix4f
                else ->
                    throw createTypeCastException(
                        value,
                        listOf(Matrix4f::class.java, Transform::class.java)
                    )
            }
        }

        override fun get() = matrixProvider

    }

    /**
     * Applies [Texture] to sampler uniform
     */
    class TextureLiveType(shaderProperty: ShaderProperty) : ShaderLiveType() {
        private val uniformId: Int = (shaderProperty as SingleShaderProperty).uniformId
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
                    throw createTypeCastException(
                        value,
                        listOf(Texture2D::class.java)
                    )
            texture = value
        }

        override fun get() = texture
    }

    /**
     * Applies [Color4f] to vec4 or vec3 uniform.
     * @param hasAlpha if `true` then color applies as vec4 otherwise as vec3.
     */
    class ColorLiveType(val hasAlpha: Boolean, shaderProperty: ShaderProperty) : ShaderLiveType() {
        private val uniformId: Int = (shaderProperty as SingleShaderProperty).uniformId
        private var color: Color4f = Color4f.white

        override fun bind(buffer: Buffer) {
            if (hasAlpha)
                GL20C.glUniform4f(uniformId, color.red, color.green, color.blue, color.alpha)
            else
                GL20C.glUniform3f(uniformId, color.red, color.green, color.blue)
        }

        override fun set(value: Any?) {
            color =
                when (value) {
                    null -> Color4f.white
                    is Color4f -> value
                    else -> throw createTypeCastException(
                        value,
                        listOf(Color4f::class.java)
                    )
                }
        }

        override fun get() = color

    }

    /**
     * Applies [Vector3f] to vec3 uniform
     */
    class Vector3LiveType(shaderProperty: ShaderProperty) : ShaderLiveType() {
        private val uniformId: Int = (shaderProperty as SingleShaderProperty).uniformId
        private var vector: Vector3f = Vector3f.zero

        override fun bind(buffer: Buffer) {
            GL20C.glUniform3f(uniformId, vector.x, vector.y, vector.z)
        }

        override fun set(value: Any?) {
            vector =
                when (value) {
                    null -> Vector3f.zero
                    is Vector3f -> value
                    else -> throw createTypeCastException(
                        value,
                        listOf(Vector3f::class.java)
                    )
                }
        }

        override fun get() = vector

    }

    /**
     * Applies [Float] to float uniform. If value if not set or set to zero, then default value will 1f.
     */
    class FloatLiveType(shaderProperty: ShaderProperty) : ShaderLiveType() {
        private val uniformId: Int = (shaderProperty as SingleShaderProperty).uniformId
        private var value: Float = 1f

        override fun bind(buffer: Buffer) {
            GL20C.glUniform1f(uniformId, value)
        }

        override fun set(value: Any?) {
            this.value = when (value) {
                null -> 1f
                is Float -> value
                else -> throw createTypeCastException(
                    value,
                    listOf(Float::class.java)
                )
            }
        }

        override fun get() = value

    }

    /**
     * Applies [LightSource] to shader structure PointLight
     */
    class LightSourceLiveType(shaderProperty: ShaderProperty) : ShaderLiveType() {
        private val intensityId: Int
        private val colourId: Int
        private val attId: Int
        private val positionId: Int

        var lightSource: LightSource? = null

        private fun Map<String, Int>.getOrThrow(paramName: String) =
            this[paramName] ?: error("Required param \'$paramName\' is not found in the uniformsMap")

        init {
            val bindings = (shaderProperty as ShaderProperty.ComplexShaderProperty).uniformsMap
            intensityId = bindings.getOrThrow("intensity")
            colourId = bindings.getOrThrow("color")
            attId = bindings.getOrThrow("attenuation")
            positionId = bindings.getOrThrow("vmPosition")
        }

        override fun bind(buffer: Buffer) {
            lightSource?.apply {
                val position = viewModelPosition
                GL20C.glUniform1f(intensityId, intensity)
                GL20C.glUniform1f(attId, if (this is PointLight) attenuation else 0f)
                GL20C.glUniform3f(colourId, color.red, color.green, color.blue)
                GL20C.glUniform3f(positionId, position.x, position.y, position.z)
            }
        }

        override fun set(value: Any?) {
            lightSource = when (value) {
                null -> null
                is LightSource -> value
                else -> throw createTypeCastException(
                    value,
                    listOf(LightSource::class.java, PointLight::class.java, DirectionalLight::class.java)
                )
            }
        }

        override fun get(): Any? = lightSource
    }

}