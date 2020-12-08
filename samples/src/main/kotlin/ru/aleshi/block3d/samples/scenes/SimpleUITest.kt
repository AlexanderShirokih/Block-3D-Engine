package ru.aleshi.block3d.samples.scenes

import ru.aleshi.block3d.Launcher
import ru.aleshi.block3d.World
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.ui.ImageFit
import ru.aleshi.block3d.ui.Insets
import ru.aleshi.block3d.ui.UIModule
import ru.aleshi.block3d.ui.ui
import ru.aleshi.block3d.ui.widget.*

fun main() {
    Launcher.start(
        SimpleUITest(),
        modules = arrayOf(UIModule)
    )
}

class SimpleUITest : Scene() {

    override fun create() {
        super.create()
        background = SolidColorBackground().apply {
            color = Color4f.white
        }

        ui.setContent(
            UIAlign(
                child = UIButton(
                    child = UIPadding(
                        padding = Insets(30f),
                        child = UIBox(
                            color = Color4f.blue,
                            child = UIImage(
                                fit = ImageFit.Expand,
                                imagePath = "textures/box.png"
                            )
                        )
                    )
                ) {
                    World.current.launchSceneAsync(TransformRelations())
                }
            )
        )
    }

}