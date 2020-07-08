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
        this.underlyingMatrix = other.underlyingMatrix.copy()
        this.changed = other.changed
    }

    /**
     * Position in world coordinates
     */
    var position: Vector3f = Vector3f()
        set(value) {
            changed = true
            field = value
        }

    /**
     * Rotation quaternion
     */
    var rotation: Quaternion = Quaternion()
        set(value) {
            changed = true
            field = value
        }

    /**
     * Object scale
     */
    var scale: Vector3f = Vector3f(1f, 1f, 1f)
        set(value) {
            changed = true
            field = value
        }

    private var changed = false

    private var underlyingMatrix = Matrix4f()

    /**
     * Returns matrix containing transformations
     */
    fun matrix(): Matrix4f {
        // Update matrix if needed
        return if (changed) {
            changed = false
            underlyingMatrix
                .identity()
                .translate(position)
                .rotate(rotation)
                .scale(scale)
        } else underlyingMatrix
    }

    /**
     * Same as [matrix], but negates position vector
     */
    fun viewMatrix(): Matrix4f {
        return if (changed) {
            changed = false

            val (x, y, z) = position

            underlyingMatrix
                .identity()
                .translate(-x, -y, -z)
                .rotate(rotation)
                .scale(scale)
        } else underlyingMatrix
    }

}
