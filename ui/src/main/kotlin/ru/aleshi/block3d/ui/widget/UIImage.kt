package ru.aleshi.block3d.ui.widget

import kotlinx.coroutines.launch
import ru.aleshi.block3d.World
import ru.aleshi.block3d.internal.data.Image2DData
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Constraint
import ru.aleshi.block3d.ui.ImageFit
import ru.aleshi.block3d.ui.UIModule
import ru.aleshi.block3d.ui.UIRenderContext

/**
 * Shows image growing as much as possible
 */
class UIImage(
    val fit: ImageFit = ImageFit.Normal,
    imagePath: String
) : UIObject() {

    private var imageData: Image2DData? = null

    init {
        World.current.worldScope.launch {
            imageData = UIModule.imageCache.getOrLoad(imagePath)
        }
    }

    override fun onMeasure(parentConstraint: Constraint): Vector2f {
        return when (fit) {
            ImageFit.Expand -> parentConstraint.maxSize
            ImageFit.Normal -> return if (imageData == null) {
                Vector2f.zero
            } else {
                Vector2f(
                    imageData!!.width.toFloat(),
                    imageData!!.height.toFloat()
                )
            }
        }
    }

    override fun onDraw(position: Vector2f, context: UIRenderContext) {
        imageData?.let { image ->
            context.drawImage(position.x, position.y, size.x, size.y, image)
        }
    }

}