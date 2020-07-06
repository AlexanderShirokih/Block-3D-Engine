package ru.aleshi.block3d

import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack
import ru.aleshi.block3d.data.ShaderData
import ru.aleshi.block3d.internal.ShaderLiveType

/**
 * Describes a shader program
 */
open class Shader {

    private var programId: Int = 0
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    private var uniforms = emptyMap<String, ShaderLiveType>()

    /**
     * Creates shader program and compiles it.
     * @exception ShaderException when shader has errors at compiling or linking or validation stage
     */
    fun create(data: ShaderData) {
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
        uniforms = fetchProperties(data.properties)
    }

    private fun createAndCompileShader(shaderType: Int, shaderSource: String): Int {
        val shaderId = glCreateShader(shaderType)

        glShaderSource(shaderId, shaderSource)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE)
            throw ShaderException(
                ShaderException.ErrorType.CompilationError,
                glGetShaderInfoLog(shaderId, 1024),
                shaderSource
            )

        return shaderId
    }

    fun setProperty(name: String, value: Any) {
        val prop = uniforms[name] ?: throw ShaderException(
            ShaderException.ErrorType.UnknownProperty, "Property \'$name\' is undefined for this shader"
        )
        prop.set(value)
    }

    /**
     * Binds current shader program
     */
    fun bind() {
        glUseProgram(programId)
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
     * Unbinds current shader program
     */
    fun unbind() {
        glUseProgram(0)
    }

    /**
     * Disposes current shaders and associated program
     */
    fun dispose() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }

    private fun fetchProperties(properties: Map<String, ShaderData.Property>) =
        properties.mapValues { entry ->
            entry.value.run {
                ShaderLiveType
                    .fromType(type, getUniformLocation(uniformName))
                    .apply { defaultValue?.also { def -> set(def) } }
            }
        }

    private fun getUniformLocation(name: String): Int {
        val uniform = glGetUniformLocation(programId, name)
        if (uniform < 0)
            throw ShaderException(ShaderException.ErrorType.UniformLocationError, "Could not find uniform:$name")
        return uniform
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return programId != (other as Shader).programId
    }

    override fun hashCode(): Int {
        return programId
    }

}