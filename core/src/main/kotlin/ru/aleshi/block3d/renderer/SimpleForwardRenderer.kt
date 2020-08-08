package ru.aleshi.block3d.renderer

import ru.aleshi.block3d.scenic.MeshObject

open class SimpleForwardRenderer : AbstractRenderer() {

    private val renderingList = mutableListOf<MeshObject>()

    override fun attachToRenderer(meshObject: MeshObject) {
        renderingList.add(meshObject)
    }

    override fun detachFromRenderer(meshObject: MeshObject) {
        renderingList.remove(meshObject)
    }

    override fun render() {
        for (meshObject in renderingList) {
            val shader = meshObject.shader
            shader.bind()
            meshObject.material.attach()
            meshObject.mesh.draw()
            shader.unbind()
        }
    }

}