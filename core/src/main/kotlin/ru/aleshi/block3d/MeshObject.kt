package ru.aleshi.block3d

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
        material.setProperty("viewModelMatrix", { Camera.active.viewMatrix * transform.matrix() })
        material.setProperty("projectionMatrix", { Camera.active.projectionMatrix })
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