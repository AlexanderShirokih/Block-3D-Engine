package ru.aleshi.block3d.types

/**
 * A class describing axis-aligned bounding box
 * @param min minimal bounds point
 * @param max maximal bounds point
 */
data class AABB(val min: Vector3f, val max: Vector3f) {

    companion object {
        @JvmStatic
        val Empty = AABB()
    }

    constructor() : this(Vector3f.zero, Vector3f.zero)

    /**
     * Bounding object size. (vector from max to min).
     */
    val size: Vector3f
        get() = max - min

    /**
     * Center point of bounds.
     */
    val center: Vector3f
        get() {
            val c = size.copy()
            c /= 2f
            c += min
            return c
        }
}