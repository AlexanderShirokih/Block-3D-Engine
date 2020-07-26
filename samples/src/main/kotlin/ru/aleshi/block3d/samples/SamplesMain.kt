package ru.aleshi.block3d.samples

import ru.aleshi.block3d.Launcher
import ru.aleshi.block3d.internal.WindowConfig
import ru.aleshi.block3d.samples.scenes.TestScene
import ru.aleshi.block3d.samples.scenes.TransformRelations

object SamplesMain {

    @JvmStatic
    fun main(args: Array<String>) {
        Launcher.start(WindowConfig(), TransformRelations())
    }
}