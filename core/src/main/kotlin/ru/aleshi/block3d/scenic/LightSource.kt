package ru.aleshi.block3d.scenic

import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector3f
import ru.aleshi.block3d.types.Vector4f
import kotlin.math.*

/**
 * Common class for light sources
 */
class LightSource(var type: Type = Type.Directional) : TransformableObject() {

    /**
     * A light type.
     * [Directional] - All rays coming from the same direction. This type not affected by attenuation.
     * [Point] - Has attenuation factor which increases by distance.
     * [Spot] - Like [Point], but emits light in a cone shape.
     */
    enum class Type {
        Directional, Point, Spot
    }

    /**
     * Light color
     */
    var color: Color4f = Color4f.white

    /**
     * Light intensity coefficient
     */
    var intensity: Float = 1f

    /**
     * Attenuation factor
     */
    var attenuation: Float = 1f

    @JvmField
    internal var cutoffCos: Float = 1.01f

    /**
     * For [Type.Spot] light defines cutoff angle. Should be in range 0..90 deg
     */
    var cutoffAngle: Float = 0f
        set(value) {
            field = max(0f, min(90f, value))
            cutoffCos = cos(field * 0.0174533f)
        }

    /**
     * Light position in view-model space
     */
    val viewModelPosition: Vector3f
        get() = (Camera.active.viewMatrix * transform.matrix()).position()

    val viewModelDirection: Vector3f
        get() {
            // Simplification for: transform.matrix() * Vector4f.forward
            val direction = transform.matrix().array().let { mat -> Vector4f(mat[8], mat[9], mat[10], 0f) }

            return (Camera.active.viewMatrix * direction).toVector3()
        }

}