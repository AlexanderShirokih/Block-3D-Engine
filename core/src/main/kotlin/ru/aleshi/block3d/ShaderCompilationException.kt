package ru.aleshi.block3d

import java.lang.Exception

/**
 * Exception class used when error happened in shader code compilation
 */
class ShaderCompilationException(shaderCode: String, errorDescription: String) :
    Exception("Error in shader: \n $shaderCode\nDescription: $errorDescription")