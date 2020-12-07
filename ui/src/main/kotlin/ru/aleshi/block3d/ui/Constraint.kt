package ru.aleshi.block3d.ui

import ru.aleshi.block3d.clamp
import ru.aleshi.block3d.types.Vector2f

/**
 * Describes constrained box
 */
data class Constraint(
    val minSize: Vector2f = Vector2f.zero,
    val maxSize: Vector2f = Vector2f.infinity
) {

    /**
     *  Bounds `desiredSize` to constraint limits
     */
    fun bounding(desiredSize: Vector2f): Vector2f {
        val x = desiredSize.x.clamp(minSize.x, maxSize.x)
        val y = desiredSize.y.clamp(minSize.y, maxSize.y)
        return Vector2f(x, y)
    }
}