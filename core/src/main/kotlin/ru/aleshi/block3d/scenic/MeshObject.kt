package ru.aleshi.block3d.scenic

import ru.aleshi.block3d.*
import ru.aleshi.block3d.shader.Shader

/**
 * An object that has a mesh, that can be rendered in a scene using a shader
 * @constructor Creates a new mesh object using [sharedMesh] and [material] that will be copied.
 */
open class MeshObject(private val sharedMesh: Shared<Mesh>, mat: Material) :
    TransformableObject() {

    internal val mesh = sharedMesh.getAndInc()

    internal var shader: Shader = mat.shader
        private set

    /**
     * Shader material instance for this object
     */
    var material: Material = mat.copy()
        set(value) {
            field = value
            shader = value.shader
            linkDefaults()
        }

    init {
        linkDefaults()
    }

    private fun linkDefaults() {
        material.setProperty("modelViewMatrix", { Camera.active.viewMatrix * transform.matrix() })
        material.setProperty("projectionMatrix", { Camera.active.projectionMatrix })
        material.setProperty("cameraPosition", { Camera.active.transform.position })
        material.setProperty("viewMatrix", { Camera.active.viewMatrix })
        material.setProperty(
            "viewMatrixAtCenter",
            {
                Camera.active.run {
                    viewMatrix.copy().apply { array().apply { this[12] = 0f; this[13] = 0f; this[14] = 0f } }
                }
            })
    }

    override fun onCreate() {
        Scene.current.attachToRenderer(this)
    }

    override fun onDestroy() {
        Scene.current.detachFromRenderer(this)
        sharedMesh.decRef()
    }

    override fun clone(): MeshObject =
        MeshObject(sharedMesh, material).also { new ->
            new.transform.set(transform)
        }

}