package ru.aleshi.block3d.shader

import java.lang.Exception

/**
 * Exception class used when error happened in shader code compilation
 */
class ShaderException(type: ErrorType, errorDescription: String, shaderCode: String = "") :
    Exception("ShaderError: $type.\nDescription: $errorDescription\n${if (shaderCode.isEmpty()) "" else "Shader code $shaderCode"}") {
    enum class ErrorType {
        SourceError,
        VertexCompilationError,
        FragmentCompilationError,
        LinkingError,
        UniformLocationError,
        PropertyTypeMismatch
    }
}