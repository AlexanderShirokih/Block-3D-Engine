package ru.aleshi.block3d.samples.scenes

import ru.aleshi.block3d.Launcher
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.Insets
import ru.aleshi.block3d.ui.Orientation
import ru.aleshi.block3d.ui.UIModule
import ru.aleshi.block3d.ui.ui
import ru.aleshi.block3d.ui.widget.UIBox
import ru.aleshi.block3d.ui.widget.UILinearGroup
import ru.aleshi.block3d.ui.widget.UIPadding

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
            UILinearGroup(
                orientation = Orientation.Horizontal,
                children = List(3) {
                    UIPadding(
                        padding = Insets(horizontal = 10f),
                        child = UILinearGroup(
                            children = List(3) {
                                UIPadding(
                                    padding = Insets(vertical = 10f),
                                    child = UIBox(
                                        preferredSize = Vector2f(100f, 50f),
                                        color = Color4f.random
                                    )
                                )
                            }
                        )
                    )
                }
            )
        )
    }

}