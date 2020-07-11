package ru.aleshi.block3d.data

import kotlinx.serialization.Serializable

/**
 * Data class describing shader program with all API variants and properties
 * @param name Unique shader name. By this name the shader will be loaded to resource list.
 * @param properties Map of properties where key is a property name, value is associated Property
 * @param programs Map of shader programs where key is a render api, value is associated ShaderProgram
 */
@Serializable
data class ShaderData(
    val name: String,
    val properties: Map<String, Property>,
    val programs: Map<RenderApi, ShaderProgram>
) {

    /**
     * A class describing single shader property
     * @param type Property type. Should be suitable with shader type
     * @param uniformName uniform name in shader code
     */
    @Serializable
    data class Property(
        val type: Type,
        val uniformName: String
    ) {
        /**
         * Enum describing property types
         */
        enum class Type {
            Float, Color, Texture2D, Vector4, Matrix4
        }
    }

    /**
     * Target render API. Currently only OpenGL 3.0 supported
     */
    enum class RenderApi {
        OpenGL30
    }

    /**
     * A class describing shader program for certain render API.
     * @param vertexShaderCode vertex shader program code
     * @param fragmentShaderCode fragment shader program code
     */
    @Serializable
    data class ShaderProgram(
        val vertexShaderCode: String,
        val fragmentShaderCode: String
    )
}