package ru.aleshi.block3d

import org.lwjgl.system.MemoryStack
import ru.aleshi.block3d.internal.ShaderLiveType

/**
 * Describes automatically bound uniforms for shader program
 */
class ShaderBindings(shader: Shader) {

    val hash = shader.hashCode()

    private val uniforms = shader.properties
        .map {
            it.name to ShaderLiveType.fromType(it.type, it.uniformId).apply {
                it.defaultValue?.let { def -> set(def) }
            }
        }
        .toMap()

    /**
     * Binds uniforms to shader
     */
    internal fun attach() {
        // Bind uniforms
        MemoryStack.stackPush().use { stack ->
            val matrixBuffer = stack.mallocFloat(16)

            uniforms.values.forEach { prop ->
                // Dump the matrix into a float buffer
                prop.bind(matrixBuffer)
            }
        }
    }

    /**
     * Links property named [name] with value [value]
     * @throws ShaderException if property with name [name] cannot be linked(type mismatch)
     */
    fun setProperty(name: String, value: Any) {
        uniforms[name]?.set(value)
    }
}