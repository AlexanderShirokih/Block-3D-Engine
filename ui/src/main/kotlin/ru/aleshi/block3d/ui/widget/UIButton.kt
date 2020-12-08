package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.input.Input
import ru.aleshi.block3d.input.MouseButton
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Interactive widget that handles user clicks
 */
class UIButton(
    val child: UIObject,
    val onPressed: () -> Unit
) : UIObject() {

    private var prevIsInBounds = false
    private var isInBounds = false

    override fun onMeasure(parentConstraint: Constraint) =
        child.measure(parentConstraint)

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        child.onDraw(position, context)

        prevIsInBounds = isInBounds
        isInBounds = if (Input.isButtonDown(MouseButton.LEFT)) {
            val (inputX, inputY) = Input.getInputPosition()
            val (posX, posY) = position
            (inputX >= posX
                    && inputY >= posY
                    && inputX <= posX + size.x
                    && inputY <= posY + size.y)
        } else {
            false
        }

        if (isInBounds && !prevIsInBounds) {
            onPressed()
        }
    }
}