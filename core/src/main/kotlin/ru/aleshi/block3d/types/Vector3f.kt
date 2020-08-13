package ru.aleshi.block3d.types

import kotlin.math.sqrt
import kotlin.random.Random

/**
 * A class used to describe 3d point in space
 */
data class Vector3f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f
) {

    constructor(value: Float) : this(value, value, value)

    companion object {
        /**
         * Returns random vector with each axis value in range [0;1)
         */
        val random: Vector3f
            get() = Vector3f(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

        @JvmField
        val one = Vector3f(1f, 1f, 1f)

        @JvmField
        val zero = Vector3f(0f, 0f, 0f)

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
     * Subtracts two vectors and returns new vector as result
     */
    operator fun minus(v: Vector3f): Vector3f {
        return Vector3f(x - v.x, y - v.y, z - v.z)
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
     * Adds two vectors
     */
    operator fun minusAssign(v: Vector3f) {
        x -= v.x
        y -= v.y
        z -= v.z
    }

    /**
     * Multiplies this vector by factor and returns new vector
     */
    operator fun times(factor: Float): Vector3f = Vector3f(x * factor, y * factor, z * factor)

    /**
     * Multiplies this vector by factor and returns this vector
     */
    operator fun timesAssign(factor: Float) {
        x *= factor
        y *= factor
        z *= factor
    }

    /**
     * Divides this vector by factor and returns new vector
     */
    operator fun div(scalar: Float): Vector3f = Vector3f(x / scalar, y / scalar, z / scalar)

    /**
     * Divides this vector by factor and returns this vector
     */
    operator fun divAssign(scalar: Float) {
        x /= scalar
        y /= scalar
        z /= scalar
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
     * Normalizes this vector and return it
     */
    fun normalize(): Vector3f {
        divAssign(magnitude)
        return this
    }


    /**
     * Returns normalized copy of this vector
     */
    val normalized: Vector3f
        get() = copy().normalize()

    /**
     * Returns vector length
     */
    val magnitude: Float
        get() = sqrt(x * x + y * y + z * z)

}