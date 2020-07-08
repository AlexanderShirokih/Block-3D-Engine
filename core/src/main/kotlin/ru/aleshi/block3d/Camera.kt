package ru.aleshi.block3d

import ru.aleshi.block3d.types.Matrix4f
import kotlin.math.abs

/**
 * A camera that you can watch the scene through. At one time only one camera can be active (renderable).
 */
class Camera : TransformableObject() {

    private var shouldUpdateMatrix = true

    internal val projectionMatrix = Matrix4f()

    companion object {
        var active: Camera = Camera()
    }

    /**
     * Vertical field of view (FOV) angle.
     */
    var fov: Float = 60f
        set(value) {
            field = value.repeat(360f)
            shouldUpdateMatrix = true
        }

    /**
     * Near clipping plane distance
     */
    var near: Float = 0.1f
        set(value) {
            field = value
            shouldUpdateMatrix = true
        }

    /**
     * Far clipping plane distance
     */
    var far: Float = 1000f
        set(value) {
            field = value
            shouldUpdateMatrix = true
        }


    /**
     * Returns `true` if the current camera is active
     */
    val isActive: Boolean = active == this

    private var aspect = 1f

    internal fun onResize(width: Float, height: Float) {
        aspect = width / height
        shouldUpdateMatrix = true
    }

    /**
     * Makes this camera renderable.
     */
    fun makeCurrent() {
        if (this == active)
            return

        // Steal current aspect ratio from previous camera
        if (abs(aspect - active.aspect) > 0.0001f) {
            aspect = active.aspect
            shouldUpdateMatrix = true
        }

        active = this
    }

    override fun onUpdate() {
        if (shouldUpdateMatrix) {
            shouldUpdateMatrix = false
            projectionMatrix.perspective(fov, aspect, near, far)
        }
    }
}