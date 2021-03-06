package ru.aleshi.block3d.scenic

import ru.aleshi.block3d.Transform

/**
 * Base class for objects that can have position in world space. Also this kind of objects can have childs
 */
open class TransformableObject : SceneObject(), Iterable<TransformableObject> {

    /**
     * Object world space transform
     */
    val transform = Transform()

    private val children = mutableListOf<TransformableObject>()

    /**
     * Gets parent object, if it exists. For root objects will be `null`.
     * Sets the new parent object of this child. If this child already has a parent then
     * the previous parent will be replaced. `null` to detach parent.
     * Note that if the parent object was removed, object adds to root of the scene.
     */
    var parent: TransformableObject? = null
        set(newParent) {
            if (newParent == field)
                return

            // Remove previous parent
            field?.children?.remove(this)

            transform.parent = newParent?.transform
            newParent?.children?.add(this)
            field = newParent
        }

    override fun update() {
        val wasChanged = transform.updateMatrix()

        super.update()

        if (children.isNotEmpty())
            if (wasChanged) for (child in children) {
                child.transform.invalidate()
                child.update()
            }
            else for (child in children)
                child.update()
    }

    override fun destroy() {
        forEach { child -> child.destroy() }
    }

    override fun onCreate() = Unit

    override fun onUpdate() = Unit

    override fun onPostUpdate() = Unit

    override fun onDestroy() = Unit

    /**
     * Creates deep copy of this object
     */
    open fun clone(): TransformableObject {
        return TransformableObject().also { new -> new.transform.set(transform) }
    }

    override fun iterator(): Iterator<TransformableObject> = children.iterator()
}