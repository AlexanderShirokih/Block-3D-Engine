package ru.aleshi.block3d.ui

import ru.aleshi.block3d.Block3DModule
import ru.aleshi.block3d.Window
import ru.aleshi.block3d.World
import ru.aleshi.block3d.scenic.Scene

/**
 * Main module class for registering module instance
 */
object UIModule : Block3DModule {
    private lateinit var layoutManager: LayoutManager
    private lateinit var drawingContext: UIRenderContext

    override fun onWindowCreated(window: Window) {
        drawingContext = NanoVGRenderContext()
        drawingContext.createDrawingContext(window.config)
        layoutManager = LayoutManager(drawingContext)
    }

    override fun onWindowDestroyed() {
        drawingContext.destroyDrawingContext()
    }

    override fun onUpdate(world: World) {
        val currentWindow = world.window
        val width = currentWindow.width.toFloat()
        val height = currentWindow.height.toFloat()

        layoutManager.measure(width, height)
        drawingContext.beginFrame(width, height)
        layoutManager.renderUI()
        drawingContext.endFrame()
    }

    override fun onSceneStarted(scene: Scene) {
        scene.add(layoutManager.root)
    }

    override fun onSceneFinished(scene: Scene) {
        scene.remove(layoutManager.root)
    }

}