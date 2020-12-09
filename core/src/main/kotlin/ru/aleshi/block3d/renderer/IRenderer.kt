package ru.aleshi.block3d.renderer

import ru.aleshi.block3d.scenic.MeshObject

/**
 * Renderer is used to draw all object on the scene
 */
interface IRenderer {

    /**
     * Registers mesh object in the renderer for rendering.
     */
    fun attachToRenderer(meshObject: MeshObject)

    /**
     * Removes previously registered mesh from rendering
     */
    fun detachFromRenderer(meshObject: MeshObject)

    /**
     * Renders all active meshes to the screen
     */
    fun render()

}