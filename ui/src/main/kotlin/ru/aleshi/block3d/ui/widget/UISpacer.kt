package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Creates an empty space with fixed size.
 */
class UISpacer(
    val space: Vector2f,
) : UIObject() {

    constructor(
        width: Float = 0f,
        height: Float = 0f
    ) : this(Vector2f(width, height))

    override fun onMeasure(parentConstraint: Constraint): Vector2f =
        parentConstraint.bounding(space)

    override fun onDraw(position: Vector2f, context: UIRenderContext) = Unit

}