package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.CrossAxisAlignment
import ru.aleshi.block3d.ui.Orientation
import ru.aleshi.block3d.ui.UIRenderContext
import kotlin.math.max

/**
 * Layouts children in a column (when orientation is [Orientation.Vertical])
 * or in a row (when orientation is [Orientation.Horizontal])
 */
class UILinearGroup(
    val orientation: Orientation = Orientation.Vertical,
    val crossAxisAlignment: CrossAxisAlignment = CrossAxisAlignment.Start,
    val children: List<UIObject> = listOf()
) : UIObject() {

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        var maxX = parentConstraint.minSize.x
        var maxY = parentConstraint.minSize.y

        var availableSpace = parentConstraint.maxSize
        for (child in children) {
            val desired = child.measure(Constraint(maxSize = availableSpace))

            val occupied = when (orientation) {
                Orientation.Vertical -> {
                    maxX = max(maxX, desired.x)
                    Vector2f(0f, desired.y)
                }
                Orientation.Horizontal -> {
                    maxY = max(maxY, desired.y)
                    Vector2f(desired.x, 0f)
                }
            }

            availableSpace = (availableSpace - occupied).max(Vector2f.zero)
            if (availableSpace == Vector2f.zero)
                break
        }
        return (parentConstraint.maxSize - availableSpace).max(Vector2f(maxX, maxY))
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        val crossAxisMask = when (orientation) {
            Orientation.Vertical -> Vector2f.right
            Orientation.Horizontal -> Vector2f.up
        }

        var currentPosition = position
        for (child in children) {
            val childSize = child.size
            val startOffset = when (crossAxisAlignment) {
                CrossAxisAlignment.Start -> Vector2f.zero
                CrossAxisAlignment.End -> (size - childSize).scale(crossAxisMask)
                CrossAxisAlignment.Center -> (size - childSize).scale(crossAxisMask) / 2f
            }

            child.onDraw(currentPosition + startOffset, context)

            val occupied = when (orientation) {
                Orientation.Vertical -> Vector2f(0f, childSize.y)
                Orientation.Horizontal -> Vector2f(childSize.x, 0f)
            }
            currentPosition = currentPosition + occupied
        }
    }
}