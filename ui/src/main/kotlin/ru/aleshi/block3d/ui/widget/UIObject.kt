package ru.aleshi.block3d.ui.widget

import ru.aleshi.block3d.scenic.SceneObject
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Base class for all UI object hierarchy
 */
abstract class UIObject : SceneObject() {

    private var _measuredSize: Vector2f = Vector2f.zero

    /**
     * Returns measured object size
     */
    val size: Vector2f
        get() = _measuredSize

    override fun onCreate() {
    }

    override fun onPostUpdate() = Unit
    override fun onDestroy() = Unit
    override fun onUpdate() = Unit

    /**
     * Lay-outs UI component. This function gives parent object constraint
     * and returns your preferred size.
     * Don't call this method directly. use [measure] instead
     */
    abstract fun onMeasure(parentConstraint: Constraint): Vector2f

    fun measure(parentConstraint: Constraint): Vector2f {
        _measuredSize = onMeasure(parentConstraint)
        return _measuredSize
    }

    /**
     * Called after measure to draw the object at `position`
     */
    abstract fun onDraw(position: Vector2f, context: UIRenderContext)

}