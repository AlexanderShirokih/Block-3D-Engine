package ru.aleshi.block3d.types

import kotlin.math.sqrt
import kotlin.random.Random

/**
 * A class used to describe 3d point in space
 */
data class Vector4f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 0f
) {

    /**
     * Constructs new vector with position [x=value; y=value; z=value; w=0.0]
     */
    constructor(value: Float) : this(value, value, value)

    /**
     * Constructs new vector from [Vector3f] and [w] component
     */
    constructor(v: Vector3f, w: Float) : this(v.x, v.y, v.z, w)

    companion object {
        /**
         * Returns random vector with each axis value in range [0;1)
         */
        val random: Vector4f
            get() = Vector4f(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

        @JvmField
        val one = Vector4f(1f, 1f, 1f)

        @JvmField
        val zero = Vector4f(0f, 0f, 0f)

        @JvmField
        val right = Vector4f(1f, 0f, 0f)

        @JvmField
        val up = Vector4f(0f, 1f, 0f)

        @JvmField
        val forward = Vector4f(0f, 0f, 1f)
    }

    /**
     * Adds two vectors and returns new vector as result
     */
    operator fun plus(v: Vector4f): Vector4f {
        return Vector4f(x + v.x, y + v.y, z + v.z, w + v.w)
    }

    /**
     * Subtracts two vectors and returns new vector as result
     */
    operator fun minus(v: Vector4f): Vector4f {
        return Vector4f(x - v.x, y - v.y, z - v.z, w - v.w)
    }

    /**
     * Adds two vectors
     */
    operator fun plusAssign(v: Vector4f) {
        x += v.x
        y += v.y
        z += v.z
        w += v.w
    }

    /**
     * Adds two vectors
     */
    operator fun minusAssign(v: Vector4f) {
        x -= v.x
        y -= v.y
        z -= v.z
        w -= v.w
    }

    /**
     * Scales this vector by factor and returns new vector
     */
    operator fun times(factor: Float): Vector4f {
        return Vector4f(x * factor, y * factor, z * factor, w * factor)
    }

    /**
     * Scales this vector by factor and returns this vector
     */
    operator fun timesAssign(factor: Float) {
        x *= factor
        y *= factor
        z *= factor
        w *= factor
    }

    /**
     * Finds dot product of two vectors
     */
    fun dot(v: Vector4f): Float {
        return x * v.x + y * v.y + z * v.z + w * v.w
    }

    /**
     * Normalizes this vector and return it
     */
    fun normalize(): Vector4f {
        val mag = magnitude
        x /= mag
        y /= mag
        z /= mag
        w /= mag
        return this
    }

    /**
     * Converts this vector to [Vector3f]
     */
    fun toVector3(): Vector3f = Vector3f(x, y, z)

    /**
     * Returns normalized copy of this vector
     */
    val normalized: Vector4f
        get() = copy().normalize()

    /**
     * Returns vector length
     */
    val magnitude: Float
        get() = sqrt(x * x + y * y + z * z + w * w)

}