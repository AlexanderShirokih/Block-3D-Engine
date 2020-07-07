package ru.aleshi.block3d

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
     * Gets parent object, if exists. For root objects will be `null`.
     * Sets the new parent object of this child.
     * If this child already have parent then previous parent will be replaced. `null` to detach parent
     */
    var parent: TransformableObject? = null
        set(newParent) {
            // Remove previous parent
            parent?.children?.remove(this)

            newParent?.children?.add(this)
            field = newParent
        }


    override fun update() {
        //TODO: Handle matrices at this level
        // update current matrix
        onUpdate()

        for (child in this) {
            // update child matrix
            child.onUpdate()
        }
    }

    override fun onCreate() = Unit

    override fun onUpdate() = Unit

    override fun onDelete() {
        forEach { child -> child.onDelete() }
    }

    /**
     * Creates deep copy of this object
     */
    open fun clone(): TransformableObject {
        return TransformableObject().also { new -> new.transform.set(transform) }
    }

    override fun iterator(): Iterator<TransformableObject> = children.iterator()
}