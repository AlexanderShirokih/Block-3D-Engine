package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Represents simple colored rectangle. Same as [UIBox] but tights its constraint
 */
class UITightBox(
    /**
     * Box fill color
     */
    var color: Color4f = Color4f.black,

    /**
     * Children UI object
     */
    var child: UIObject
) : UIObject() {

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        return child.measure(parentConstraint)
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        context.drawRect(position.x, position.y, size.x, size.y, color)
        child.onDraw(position, context)
    }

}