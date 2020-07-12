package ru.aleshi.block3d.renderer

import ru.aleshi.block3d.MeshObject

/**
 * Renderer is used to draw all object on the scene
 */
abstract class AbstractRenderer {

    /**
     * Registers mesh object in the renderer for rendering.
     */
    abstract fun attachToRenderer(meshObject: MeshObject)

    /**
     * Removes previously registered mesh from rendering
     */
    abstract fun detachFromRenderer(meshObject: MeshObject)

    /**
     * Renders all active meshes to the screen
     */
    abstract fun render()

}