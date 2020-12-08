package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIModule
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Widget that simply displays the [text] with [color] and [fontSize]
 */
class UIText(
    val color: Color4f = Color4f.black,
    val fontSize: Float = 14.0f,
    val text: String
) : UIObject() {

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        return parentConstraint.bounding(
            UIModule.renderContext().measureText(parentConstraint.maxSize.x, fontSize, text)
        )
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        if (text.isNotEmpty()) {
            context.drawText(position.x, position.y, size.x, fontSize, color, text)
        }
    }

}