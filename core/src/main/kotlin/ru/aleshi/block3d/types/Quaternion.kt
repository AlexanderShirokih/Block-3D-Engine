package ru.aleshi.block3d.types

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A class describing rotation
 */
data class Quaternion(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 1f
) {

    companion object {

        /**
         * Sets quaternion from Euler angles
         */
        @JvmStatic
        fun fromEuler(x: Float, y: Float, z: Float): Quaternion {
            val qx = fromAxisAngle(Vector3f.right, x)
            val qy = fromAxisAngle(Vector3f.up, y)
            val qz = fromAxisAngle(Vector3f.forward, z)

            val t = qx * qy
            return t * qz
        }

        /**
         * Sets quaternion from 3d axis and angle
         * @param axis rotation axis
         * @param angle angle in degrees
         */
        @JvmStatic
        fun fromAxisAngle(axis: Vector3f, angle: Float): Quaternion {
            val a = Math.toRadians(angle.toDouble()) / 2
            val sinA = sin(a)
            val cosA = cos(a)

            val x = (axis.x * sinA).toFloat()
            val y = (axis.y * sinA).toFloat()
            val z = (axis.z * sinA).toFloat()
            val w = cosA.toFloat()

            return Quaternion(x, y, z, w).normalize()
        }
    }


    /**
     * Multiplies two quaternions and returns the result quaternion
     */
    operator fun times(q: Quaternion): Quaternion {
        // (a, u) * (b, v) = (a*b - u.v, a*v + b*u + uXv)
        val vx = w * q.x + x * q.w + y * q.z - z * q.y
        val vy = w * q.y + y * q.w + z * q.x - x * q.z
        val vz = w * q.z + z * q.w + x * q.y - y * q.x
        val sc = w * q.w - x * q.x - y * q.y - z * q.z
        return Quaternion(vx, vy, vz, sc)
    }

    /**
     * Returns quaternion length
     */
    val magnitude: Float
        get() = sqrt(w * w + x * x + y * y + z * z)

    /**
     * Normalizes this quaternion and returns this quaternion as result
     */
    fun normalize(): Quaternion {
        val length = magnitude
        x /= length
        y /= length
        z /= length
        w /= length
        return this
    }

}