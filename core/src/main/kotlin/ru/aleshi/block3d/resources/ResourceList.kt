package ru.aleshi.block3d.resources

import ru.aleshi.block3d.IDisposable
import ru.aleshi.block3d.Shader
import ru.aleshi.block3d.data.ShaderData

/**
 * A resource list for managing various resource types, such as shaders, textures.
 * @param parent when set, the current resource list will search in the parent resource list if the resource wasn't found in it.
 */
class ResourceList(private val parent: ResourceList? = null) : IDisposable {

    private val shaders = mutableMapOf<String, Shader>()

    companion object {
        val default: ResourceList = ResourceList()

        suspend fun loadDefaultResources() {
            if (default.shaders.isNotEmpty())
                throw RuntimeException("Default resource list is not empty!")

            val shaders = listOf("simple_unlit.shc")

            for (shaderName in shaders) {
                val shaderData = Loader.loadResource("shaders/$shaderName") as ShaderData
                default.addResource(shaderData.name, Shader(shaderData))
            }
        }
    }

    /**
     * Adds resource to this resource list.
     * @throws ResourceAlreadyExistsException If resource with name already exists in the corresponding category.
     * @throws RuntimeException If the resource cannot be assigned to a category
     */
    fun addResource(name: String, resource: Any) {
        val res = when (resource) {
            is Shader -> shaders.putIfAbsent(name, resource) to "shader"
            else -> throw RuntimeException("The resource with type ${resource.javaClass} cannot be assigned to a category")
        }

        if (res.first != null)
            throw ResourceAlreadyExistsException(name, res.second)
    }

    /**
     * Finds shader with name [name].
     * @return `null` if shader wasn't found or shader instance
     */
    fun findShader(name: String): Shader? = shaders[name] ?: (parent?.findShader(name))

    /**
     * Finds shader with name [name].
     * @throws ResourceNotFoundException if shader wasn't found.
     */
    fun requireShader(name: String): Shader = findShader(name) ?: throw ResourceNotFoundException(name)

    override fun dispose() {
        for (shader in shaders.values)
            shader.dispose()
        shaders.clear()
    }
}