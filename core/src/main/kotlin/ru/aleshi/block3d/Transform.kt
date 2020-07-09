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
     * Position in world coordinates
     */
    var position: Vector3f = Vector3f()
        set(value) {
            hasChanges = true
            field = value
        }

    /**
     * Rotation quaternion
     */
    var rotation: Quaternion = Quaternion()
        set(value) {
            hasChanges = true
            field = value
        }

    /**
     * Object scale
     */
    var scale: Vector3f = Vector3f(1f, 1f, 1f)
        set(value) {
            hasChanges = true
            field = value
        }

    /**
     * `true` if transform was changes since last frame
     */
    internal var hasChanges = false

    private var underlyingMatrix = Matrix4f()

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
    fun matrix(): Matrix4f {
        // Update matrix if needed
        val parentHasChanges = parent?.hasChanges ?: false

        return if (hasChanges || parentHasChanges) {
            val base =
                if (parent == null) underlyingMatrix.identity()
                else underlyingMatrix.set(parent!!.underlyingMatrix)

            base
                .translate(position)
                .rotate(rotation)
                .scale(scale)
        } else underlyingMatrix
    }

    /**
     * Same as [matrix], but negates position vector
     */
    fun viewMatrix(): Matrix4f {
        return if (hasChanges) {
            hasChanges = false

            val (x, y, z) = position

            underlyingMatrix
                .identity()
                .translate(-x, -y, -z)
                .rotate(rotation)
                .scale(scale)
        } else underlyingMatrix
    }

}
