package ru.aleshi.block3d.ui

import ru.aleshi.block3d.Block3DModule
import ru.aleshi.block3d.Window
import ru.aleshi.block3d.World
import ru.aleshi.block3d.resources.BinaryLoader
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.ui.internal.ImageCache
import ru.aleshi.block3d.ui.internal.NanoVGRenderContext

/**
 * Main module class for registering module instance
 */
object UIModule : Block3DModule {
    private lateinit var layoutManager: LayoutManager
    private lateinit var drawingContext: UIRenderContext

    private var isStarted = false

    val imageCache = ImageCache()

    override fun onWindowCreated(window: Window) {
        Loader.installParser("ttf", BinaryLoader::class.java)
        drawingContext = NanoVGRenderContext()
        drawingContext.createDrawingContext(window.config)
    }

    /**
     * Returns current render context
     */
    fun renderContext(): UIRenderContext = drawingContext

    override suspend fun onInit() =
        drawingContext.initResources()

    override fun onWindowDestroyed() {
        drawingContext.destroyDrawingContext()
    }

    override fun onUpdate(world: World) {
        if (!isStarted) return

        val currentWindow = world.window
        val width = currentWindow.width.toFloat()
        val height = currentWindow.height.toFloat()

        layoutManager.measure(width, height)
        drawingContext.beginFrame(width, height)
        layoutManager.renderUI()
        drawingContext.endFrame()
    }

    override fun onSceneStarted(scene: Scene) {
        layoutManager = LayoutManager(drawingContext)
        scene.add(layoutManager.root)
        isStarted = true
    }

    override fun onSceneFinished(scene: Scene) {
        isStarted = false
        scene.remove(layoutManager.root)
        imageCache.cleanup()
    }

}