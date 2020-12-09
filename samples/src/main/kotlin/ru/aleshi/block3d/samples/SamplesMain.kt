package ru.aleshi.block3d.samples

import ru.aleshi.block3d.Launcher
import ru.aleshi.block3d.samples.scenes.MainScene
import ru.aleshi.block3d.ui.UIModule

object SamplesMain {
    @JvmStatic
    fun main(args: Array<String>) {
        Launcher.start(
            MainScene(),
            modules = arrayOf(UIModule)
        )
    }
}
