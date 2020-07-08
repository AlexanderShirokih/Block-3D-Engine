package ru.aleshi.block3d

import org.lwjgl.system.MemoryStack
import ru.aleshi.block3d.internal.ShaderLiveType
import ru.aleshi.block3d.internal.ShaderLiveType.TextureLiveType

/**
 * Describes automatically bound uniforms for shader program
 */
class ShaderBindings(shader: Shader) {

    val hash = shader.hashCode()

    private val uniforms = shader.properties
        .map { it.name to ShaderLiveType.fromType(it.type, it.uniformId) }
        .toMap().apply {
            // Setup texture unit slot indexes
            values
                .asSequence()
                .filter { it is TextureLiveType }
                .forEachIndexed { index, shaderLiveType -> (shaderLiveType as TextureLiveType).slot = index }
        }

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
     * Links property named [name] with value [value]. If a property with [name] does not exist, nothing will be done.
     * @throws ShaderException if property with name [name] cannot be linked(type mismatch)
     */
    fun setProperty(name: String, value: Any) {
        uniforms[name]?.set(value)
    }
}