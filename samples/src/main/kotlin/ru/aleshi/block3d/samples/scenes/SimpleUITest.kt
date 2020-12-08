package ru.aleshi.block3d.samples.scenes

import ru.aleshi.block3d.Launcher
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.scenic.SolidColorBackground
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.UIModule
import ru.aleshi.block3d.ui.ui
import ru.aleshi.block3d.ui.widget.UIAlign
import ru.aleshi.block3d.ui.widget.UIBox
import ru.aleshi.block3d.ui.widget.UIButton
import ru.aleshi.block3d.ui.widget.UIText

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
                    child = UIBox(
                        color = Color4f.blue,
                        preferredSize = Vector2f(140f, 100f),
                        child = UIAlign(
                            child = UIText(
                                fontSize = 24f,
                                text = "Hello world!"
                            )
                        )
                    )
                ) {
                    println("I'm pressed!")
                }
            )
        )
    }

}