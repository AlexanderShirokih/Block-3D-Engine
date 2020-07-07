package ru.aleshi.block3d

class MeshObject(val mesh: Mesh, val shader: Shader) : TransformableObject() {

    override fun onUpdate() {
        shader.bind()
        mesh.draw()
        shader.unbind()
    }

    override fun onDelete() {
        //TODO: disposing mesh will affect all copies of this mesh. We need to create something reference-counted
//        mesh.dispose()
    }

    override fun clone(): MeshObject =
        MeshObject(mesh.copy(), shader.copy()).also { new -> new.transform.set(transform) }
}