package ru.aleshi.block3d

class MeshObject(private val sharedMesh: Shared<Mesh>, private val sharedShader: Shared<Shader>) :
    TransformableObject() {

    private val mesh = sharedMesh.getAndInc()
    private val shader = sharedShader.getAndInc()

    /**
     * Shader bindings instance for this object
     */
    val bindings = ShaderBindings(shader)

    override fun onUpdate() {
        shader.bind()
        bindings.attach()
        mesh.draw()
        shader.unbind()
    }

    override fun onDelete() {
        sharedMesh.decRef()
        sharedShader.decRef()
    }

    override fun clone(): MeshObject =
        MeshObject(sharedMesh, sharedShader).also { new -> new.transform.set(transform) }

}