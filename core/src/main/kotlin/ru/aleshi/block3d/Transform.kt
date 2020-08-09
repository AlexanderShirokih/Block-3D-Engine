package ru.aleshi.block3d

import ru.aleshi.block3d.types.Matrix4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

/**
 * Describes object location in 3D space.
 */
class Transform {

    /**
     * Sets this transform from another transform
     */
    fun set(other: Transform) {
        this.position = other.position.copy()
        this.rotation = other.rotation.copy()
        this.scale = other.scale.copy()
        this.underlyingMatrix.set(underlyingMatrix)
        this.hasChanges = other.hasChanges
    }

    /**
     * Position in local coordinates
     */
    var position: Vector3f = Vector3f()
        set(value) {
            hasChanges = true
            field = value
        }

    /**
     * Translates position to [v]
     */
    fun translate(v: Vector3f) {
        position.plusAssign(v)
        hasChanges = true
    }

    /**
     * Rotation quaternion in local coordinates
     */
    var rotation: Quaternion = Quaternion()
        set(value) {
            hasChanges = true
            field = value
        }

    /**
     * Adds rotation by quaternion [q]
     */
    fun rotate(q: Quaternion) {
        rotation.timesAssign(q)
        hasChanges = true
    }

    /**
     * Object scale in local coordinates
     */
    var scale: Vector3f = Vector3f(1f, 1f, 1f)
        set(value) {
            hasChanges = true
            field = value
        }


    /**
     * Multiplies current object scale to factor [s]
     */
    fun scale(s: Float) {
        scale.timesAssign(s)
        hasChanges = true
    }

    /**
     * `true` if transform was changes since last frame
     */
    internal var hasChanges = false

    private var underlyingMatrix = Matrix4f()

    /**
     * Marks transform matrix for update
     */
    fun invalidate() {
        hasChanges = true
    }


    /**
     * Sets the parent transform object
     */
    var parent: Transform? = null
        set(value) {
            if (field !== value)
                hasChanges = true
            field = value
        }

    /**
     * Returns matrix containing transformations
     */
    fun matrix(): Matrix4f = underlyingMatrix

    /**
     * Recalculates object matrix if needed.
     * @return `true` if matrix was updates
     */
    fun updateMatrix(): Boolean {
        if (hasChanges) {
            val base =
                if (parent == null) underlyingMatrix.identity()
                else underlyingMatrix.set(parent!!.underlyingMatrix)

            base
                .translate(position)
                .rotate(rotation)
                .scale(scale)

            hasChanges = false
            return true
        }
        return false
    }

}
