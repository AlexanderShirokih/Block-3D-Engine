package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Scene root object. Always expands whole screen size
 */
class UIRootObject(private val uiRenderContext: UIRenderContext) : UIObject() {

    internal var child: UIObject? = null

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        // Root object always fit whole screen size
        child?.measure(parentConstraint)
        return parentConstraint.maxSize
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        throw IllegalStateException("Could not draw UIRootObject from non system origin")
    }

    fun drawLayoutTree() {
        child?.onDraw(Vector2f.zero, uiRenderContext)
    }

    override fun onUpdate() = Unit

    /**
     * Clears child
     */
    fun clear() {
        child = null
    }

    /**
     * Sets the current child object
     */
    fun setContent(child: UIObject) {
        this.child = child
    }
}