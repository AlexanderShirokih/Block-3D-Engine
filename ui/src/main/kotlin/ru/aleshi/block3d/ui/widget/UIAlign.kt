package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Alignment
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Align child object in parent container.
 * This widget takes all available space of parent.
 */
class UIAlign(
    var alignment: Alignment = Alignment.center,
    var child: UIObject
) : UIObject() {

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        child.measure(parentConstraint)
        return parentConstraint.maxSize
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        child.onDraw(getPositionFromAlignment(position), context)
    }

    private fun getPositionFromAlignment(position: Vector2f): Vector2f {
        val realAlignX = (alignment.xAlignment + 1f) / 2f
        val realAlignY = (alignment.yAlignment + 1f) / 2f

        return Vector2f(
            position.x + (size.x - child.size.x) * realAlignX,
            position.y + (size.y - child.size.y) * realAlignY
        )
    }

}