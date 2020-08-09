package ru.aleshi.block3d.renderer

import org.lwjgl.system.MemoryStack
import ru.aleshi.block3d.Mesh
import ru.aleshi.block3d.scenic.MeshObject
import ru.aleshi.block3d.shader.Shader

open class SimpleForwardRenderer : AbstractRenderer() {

    // Use two level grouping
    private val renderingList = hashMapOf<Mesh, MutableMap<Shader, MutableSet<MeshObject>>>()

    override fun attachToRenderer(meshObject: MeshObject) {
        val mesh = meshObject.mesh
        val shader = meshObject.shader

        val shaderGroup = renderingList[mesh]
        if (shaderGroup == null) {
            renderingList[mesh] = hashMapOf(shader to mutableSetOf(meshObject))
            return
        }

        val meshSet = shaderGroup[shader]
        if (meshSet == null) {
            shaderGroup[shader] = mutableSetOf(meshObject)
            return
        }

        meshSet.add(meshObject)
    }

    override fun detachFromRenderer(meshObject: MeshObject) {
        val mesh = meshObject.mesh
        val shader = meshObject.shader

        val shaderGroups = renderingList[mesh] ?: return
        val meshSet = shaderGroups[shader] ?: return

        meshSet.remove(meshObject)
    }

    override fun render() {
        // Bind uniforms
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(16)

            for ((mesh, shaderGroup) in renderingList) {
                mesh.bind()
                for ((shader, meshObjects) in shaderGroup) {
                    shader.bind()
                    for (instance in meshObjects) {
                        instance.material.attach(buffer)
                        mesh.draw()
                    }
                    shader.unbind()
                }
                mesh.unbind()
            }
        }
    }
}