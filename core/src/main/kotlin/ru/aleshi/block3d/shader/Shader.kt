package ru.aleshi.block3d.shader

import org.lwjgl.opengl.GL20C.*
import ru.aleshi.block3d.IDisposable
import ru.aleshi.block3d.data.ShaderData

/**
 * Describes a shader program
 * @param data shader source data
 */
class Shader(data: ShaderData) : IDisposable {

    /**
     * Linked shader property with fetched uniform ids.
     */
    sealed class ShaderProperty(
        val name: String,
        val type: ShaderData.Property.Type
    ) {

        /**
         * Used when property describes a single type or system structure
         */
        class SingleShaderProperty(
            val uniformId: Int,
            name: String,
            type: ShaderData.Property.Type
        ) : ShaderProperty(name, type)

        /**
         * Used when property describes complex used-defined type
         * @param uniformsMap mapping from properties field to it's uniform
         */
        class ComplexShaderProperty(
            val uniformsMap: Map<String, Int>,
            name: String,
            type: ShaderData.Property.Type
        ) : ShaderProperty(name, type)
    }

    private var programId: Int = 0
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    internal var properties = emptyList<ShaderProperty>()
        private set

    /**
     * Creates shader program and compiles it.
     * @throws ShaderException when shader has errors at compiling or linking or validation stage
     */
    init {
        // Get target shader program
        val program = data.programs[ShaderData.RenderApi.OpenGL30]
            ?: throw ShaderException(
                ShaderException.ErrorType.SourceError,
                "Current shader data doesn't have shader program for current render API"
            )

        // Create shader ids and compile it
        vertexShaderId = createAndCompileShader(GL_VERTEX_SHADER, program.vertexShaderCode)
        fragmentShaderId = createAndCompileShader(GL_FRAGMENT_SHADER, program.fragmentShaderCode)

        // Create program and link shaders to it
        programId = glCreateProgram()
        glAttachShader(programId, vertexShaderId)
        glAttachShader(programId, fragmentShaderId)
        glLinkProgram(programId)

        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE)
            throw ShaderException(
                ShaderException.ErrorType.LinkingError,
                glGetShaderInfoLog(programId, 1024),
                "\tvertex:\n${program.vertexShaderCode}\n\tfragment:\n${program.fragmentShaderCode}\n\t->Linking error!"
            )

        if (vertexShaderId != 0)
            glDetachShader(programId, vertexShaderId)

        if (fragmentShaderId != 0)
            glDetachShader(programId, fragmentShaderId)


        // Fetch all properties
        properties = fetchProperties(data.properties)
    }

    private fun createAndCompileShader(shaderType: Int, shaderSource: String): Int {
        val shaderId = glCreateShader(shaderType)

        glShaderSource(shaderId, shaderSource)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE)
            throw ShaderException(
                when (shaderType) {
                    GL_VERTEX_SHADER -> ShaderException.ErrorType.VertexCompilationError
                    GL_FRAGMENT_SHADER ->
                        ShaderException.ErrorType.FragmentCompilationError
                    else -> error("Unknown shader type: $shaderType")
                },
                glGetShaderInfoLog(shaderId, 1024),
                shaderSource
            )

        return shaderId
    }

    /**
     * Binds current shader program
     */
    fun bind() {
        glUseProgram(programId)
    }

    /**
     * Unbinds current shader program
     */
    fun unbind() {
        glUseProgram(0)
    }

    override fun dispose() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
            programId = 0
        }
    }

    private fun fetchProperties(properties: Map<String, ShaderData.Property>) =
        properties.map { entry ->
            entry.value.run {
                if (type.properties != null) {
                    ShaderProperty.ComplexShaderProperty(
                        uniformsMap = type.properties.asList().associateWith { getUniformLocation("$uniformName.$it") },
                        name = entry.key,
                        type = type
                    )
                } else
                    ShaderProperty.SingleShaderProperty(
                        name = entry.key,
                        uniformId = getUniformLocation(uniformName),
                        type = type
                    )
            }
        }

    private fun getUniformLocation(name: String): Int {
        val uniform = glGetUniformLocation(programId, name)
        if (uniform < 0)
            throw ShaderException(
                ShaderException.ErrorType.UniformLocationError,
                "Could not find uniform: $name. This uniform is undefined in the shader source or shader or it doesn't use it."
            )
        return uniform
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return programId != (other as Shader).programId
    }

    override fun hashCode(): Int = programId

}