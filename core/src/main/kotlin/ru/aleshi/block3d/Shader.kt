package ru.aleshi.block3d

import org.lwjgl.opengl.GL20.*

/**
 * Describes a shader program
 */
open class Shader {

    private var programId: Int = 0
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    /**
     * Creates shader program and compiles it.
     * @exception ShaderCompilationException when shader has errors at compiling or linking or validation stage
     */
    fun create(vertexShaderCode: String, fragmentShaderCode: String) {
        // Create shader ids and compile it
        vertexShaderId = createAndCompileShader(GL_VERTEX_SHADER, vertexShaderCode)
        fragmentShaderId = createAndCompileShader(GL_FRAGMENT_SHADER, fragmentShaderCode)

        // Create program and link shaders to it
        programId = glCreateProgram()
        glAttachShader(programId, vertexShaderId)
        glAttachShader(programId, fragmentShaderId)
        glLinkProgram(programId)

        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE)
            throw ShaderCompilationException(
                "\tvertex:\n$vertexShaderCode\n\tfragment:\n$fragmentShaderCode\n\t->Linking error!",
                glGetShaderInfoLog(programId, 1024)
            )

        if (vertexShaderId != 0)
            glDetachShader(programId, vertexShaderId)

        if (fragmentShaderId != 0)
            glDetachShader(programId, fragmentShaderId)

    }

    private fun createAndCompileShader(shaderType: Int, shaderSource: String): Int {
        val shaderId = glCreateShader(shaderType)

        glShaderSource(shaderId, shaderSource)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE)
            throw ShaderCompilationException(shaderSource, glGetShaderInfoLog(shaderId, 1024))

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

    /**
     * Disposes current shaders and associated program
     */
    fun dispose() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
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