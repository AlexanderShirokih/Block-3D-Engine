package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.Insets
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Adds insets to all sides of [child]
 */
class UIPadding(
    val padding: Insets,
    var child: UIObject
) : UIObject() {

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        val padding = Vector2f(padding.width, padding.height)
        return child.measure(
            parentConstraint.copy(
                maxSize = parentConstraint.maxSize - padding
            )
        ) + padding
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        child.onDraw(position + Vector2f(padding.left, padding.top), context)
    }
}