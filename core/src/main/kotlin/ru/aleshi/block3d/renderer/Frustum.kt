package ru.aleshi.block3d.renderer

import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.types.AABB
import ru.aleshi.block3d.types.Vector3f
import kotlin.math.sqrt

/**
 * Used to discard object which is not visible on the screen
 */

class Frustum {

    internal class Plane(private val a: Float, private val b: Float, private val c: Float, private val d: Float) {
        companion object {
            @JvmStatic
            fun create(a: Float, b: Float, c: Float, d: Float): Plane {
                val magnitude = sqrt(a * a + b * b + c * c + d * d)
                return Plane(
                    a / magnitude,
                    b / magnitude,
                    c / magnitude,
                    d / magnitude
                )
            }
        }

        /**
         * Finds the dot product between this plane and vector v (x,y,z,1)
         */
        fun dot(v: Vector3f) =
            a * v.x + b * v.y + c * v.z + d
    }

    private lateinit var planes: Array<Plane>

    /**
     * Extracts frustum planes from current active camera
     */
    fun updateFrustum() {
        val camera = Camera.active
        val clip = (camera.projectionMatrix * camera.viewMatrix).array()
        planes = extractFrustum(clip)
    }

    private fun extractFrustum(clip: FloatArray): Array<Plane> {
        // Extract planes from the clip matrix

        val right = Plane.create(
            clip[3] - clip[0],
            clip[7] - clip[4],
            clip[11] - clip[8],
            clip[15] - clip[12]
        )

        val left = Plane.create(
            clip[3] + clip[0],
            clip[7] + clip[4],
            clip[11] + clip[8],
            clip[15] + clip[12]
        )

        val bottom = Plane.create(
            clip[3] + clip[1],
            clip[7] + clip[5],
            clip[11] + clip[9],
            clip[15] + clip[13]
        )

        val top = Plane.create(
            clip[3] - clip[1],
            clip[7] - clip[5],
            clip[11] - clip[9],
            clip[15] - clip[13]
        )

        val back = Plane.create(
            clip[3] - clip[2],
            clip[7] - clip[6],
            clip[11] - clip[10],
            clip[15] - clip[14]
        )

        val front = Plane.create(
            clip[3] + clip[2],
            clip[7] + clip[6],
            clip[11] + clip[10],
            clip[15] + clip[14]
        )

        return arrayOf(left, right, bottom, top, front, back)
    }

    /**
     * Checks whether the bounding box is located in the frustum
     * @return `true` if [bounds] is in frustum, `false` otherwise
     */
    fun boxInFrustum(bounds: AABB, center: Vector3f): Boolean {
        val min = center + bounds.min
        val max = center + bounds.max

        return planes.all { p -> p.dot(min) >= 0f && p.dot(max) >= 0f }
    }


    /**
     * Checks whether the point is located in the frustum
     * @return `true` if [point] is in frustum, `false` otherwise
     */
    fun pointInFrustum(point: Vector3f): Boolean =
        sphereInFrustum(point, 0f)

    /**
     * Checks whether the sphere with [radius] is located in the frustum
     * @return `true` if sphere is in frustum, `false` otherwise
     */
    fun sphereInFrustum(point: Vector3f, radius: Float): Boolean =
        planes.all { plane -> plane.dot(point) > -radius }

    /**
     * Checks if any point of polygon is located in the frustum or not
     * @return `true` if any point of [points] list is is frustum, `false` otherwise
     */
    fun polygonInFrustum(points: List<Vector3f>): Boolean =
        points.any { point -> pointInFrustum(point) }
}