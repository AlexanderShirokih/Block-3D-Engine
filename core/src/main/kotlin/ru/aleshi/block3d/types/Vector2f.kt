package ru.aleshi.block3d.types

import kotlin.math.max
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * A class used to describe 2d point in plane
 */
data class Vector2f(
    var x: Float = 0f,
    var y: Float = 0f
) {

    companion object {

        /**
         * Returns random vector with each axis value in range [0;1)
         */
        val random: Vector2f
            get() = Vector2f(Random.nextFloat(), Random.nextFloat())

        @JvmField
        val one = Vector2f(1f, 1f)

        @JvmField
        val zero = Vector2f(0f, 0f)

        @JvmField
        val right = Vector2f(1f, 0f)

        @JvmField
        val up = Vector2f(0f, 1f)

        @JvmField
        val forward = Vector2f(0f, 0f)

        @JvmField
        val infinity = Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)

        @JvmField
        val negative = Vector2f(-1f, -1f)
    }

    /**
     * Adds two vectors and returns new vector as result
     */
    operator fun plus(v: Vector2f): Vector2f {
        return Vector2f(x + v.x, y + v.y)
    }

    /**
     * Subtracts two vectors and returns new vector as result
     */
    operator fun minus(v: Vector2f): Vector2f {
        return Vector2f(x - v.x, y - v.y)
    }

    /**
     * Adds two vectors
     */
    operator fun plusAssign(v: Vector2f) {
        x += v.x
        y += v.y
    }

    /**
     * Adds two vectors
     */
    operator fun minusAssign(v: Vector2f) {
        x -= v.x
        y -= v.y
    }

    /**
     * Multiplies this vector by factor and returns new vector
     */
    operator fun times(factor: Float): Vector2f = Vector2f(x * factor, y * factor)

    /**
     * Multiplies this vector by factor and returns this vector
     */
    operator fun timesAssign(factor: Float) {
        x *= factor
        y *= factor
    }

    /**
     * Divides this vector by factor and returns new vector
     */
    operator fun div(scalar: Float): Vector2f = Vector2f(x / scalar, y / scalar)

    /**
     * Divides this vector by factor and returns this vector
     */
    operator fun divAssign(scalar: Float) {
        x /= scalar
        y /= scalar
    }

    /**
     * Finds dot product of two vectors
     */
    fun dot(v: Vector2f): Float {
        return x * v.x + y * v.y
    }

    /**
     * Normalizes this vector and return it
     */
    fun normalize(): Vector2f {
        divAssign(magnitude)
        return this
    }

    fun max(max: Vector2f): Vector2f {
        return Vector2f(
            x = max(x, max.x),
            y = max(y, max.y)
        )
    }

    /**
     * Returns normalized copy of this vector
     */
    val normalized: Vector2f
        get() = copy().normalize()

    /**
     * Returns vector length
     */
    val magnitude: Float
        get() = sqrt(x * x + y * y)

}