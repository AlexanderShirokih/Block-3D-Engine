package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Stack widget occupies whole container space and layouts all
 * of its children at the same position
 */
class UIStack(
    val children: List<UIObject>
) : UIObject() {

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        for (child in children) {
            child.measure(parentConstraint)
        }
        return parentConstraint.maxSize
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        for (child in children) {
            child.onDraw(position, context)
        }
    }
}