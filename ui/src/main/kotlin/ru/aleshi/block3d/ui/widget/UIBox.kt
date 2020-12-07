package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Represents simple colored rectangle
 */
class UIBox(
    /**
     * Box fill color
     */
    var color: Color4f = Color4f.black,

    /**
     * Desired Box size
     */
    var preferredSize: Vector2f = Vector2f.infinity,

    /**
     * Children UI object
     */
    var child: UIObject? = null
) : UIObject() {

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        val size = parentConstraint.bounding(preferredSize)
        child?.measure(Constraint(maxSize = size))
        return size
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        context.drawRect(position.x, position.y, size.x, size.y, color)
        child?.onDraw(position, context)
    }

}