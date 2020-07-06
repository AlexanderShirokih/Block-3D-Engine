package ru.aleshi.block3d.types

import kotlin.math.sqrt

/**
 * A class used to describe 3d point in space
 */
data class Vector3f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f
) {

    companion object {
        @JvmField
        val right = Vector3f(1f, 0f, 0f)

        @JvmField
        val up = Vector3f(0f, 1f, 0f)

        @JvmField
        val forward = Vector3f(0f, 0f, 1f)
    }

    /**
     * Adds two vectors and returns new vector as result
     */
    operator fun plus(v: Vector3f): Vector3f {
        return Vector3f(x + v.x, y + v.y, z + v.z)
    }

    /**
     * Adds two vectors
     */
    operator fun plusAssign(v: Vector3f) {
        x += v.x
        y += v.y
        z += v.z
    }

    /**
     * Scales this vector by factor and returns new vector
     */
    operator fun times(factor: Float): Vector3f {
        return Vector3f(x * factor, y * factor, z * factor)
    }

    /**
     * Scales this vector by factor and returns this vector
     */
    operator fun timesAssign(factor: Float) {
        x *= factor
        y *= factor
        z *= factor
    }

    /**
     * Finds dot product of two vectors
     */
    fun dot(v: Vector3f): Float {
        return x * v.x + y * v.y + z * v.z
    }

    /**
     * Find cross product of two vectors
     */
    fun cross(b: Vector3f): Vector3f {
        return Vector3f(
            y * b.z - z * b.y,
            z * b.x - x * b.z,
            x * b.y - y * b.x
        )
    }

    /**
     * Returns vector length
     */
    val magnitude: Float
        get() = sqrt(x * x + y * y + z * z)

}