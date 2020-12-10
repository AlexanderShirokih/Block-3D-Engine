package ru.aleshi.block3d.ui.internal

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.system.MemoryUtil.NULL
import ru.aleshi.block3d.Texture2D
import ru.aleshi.block3d.internal.WindowConfig
import ru.aleshi.block3d.internal.data.Image2DData
import ru.aleshi.block3d.resources.Loader
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector2f
import ru.aleshi.block3d.ui.UIRenderContext
import java.nio.ByteBuffer

/**
 * Creates UI rendering context using NanoVG library
 */
class NanoVGRenderContext : UIRenderContext {

    data class LoadedImage(
        val handle: Int,
        val pattern: NVGPaint,
        val width: Int,
        val height: Int
    )

    private val loadedImages = mutableMapOf<Image2DData, LoadedImage>()
    private val defaultFont = "OpenSans-Regular"

    /// Internal pointer to VG context
    private var vg: Long = NULL

    /// Temp color reference used to pass color to VG
    private lateinit var colour: NVGColor

    override fun createDrawingContext(config: WindowConfig) {
        this.vg = if (config.antialiasing)
            nvgCreate(NVG_ANTIALIAS or NVG_STENCIL_STROKES)
        else
            nvgCreate(NVG_STENCIL_STROKES)

        if (vg == NULL) {
            throw Exception("Could not init NanoVG")
        }

        colour = NVGColor.create()
    }

    override fun destroyDrawingContext() {
        if (vg != NULL) {
            for (imageHandle in loadedImages) {
                nvgDeleteImage(vg, imageHandle.value.handle)
            }
            loadedImages.clear()

            nvgDelete(vg)
            vg = NULL
        }
    }

    override fun beginFrame(width: Float, height: Float) {
        val devicePixelRatio = 1f
        nvgBeginFrame(vg, width, height, devicePixelRatio)
    }

    override fun endFrame() {
        nvgEndFrame(vg)
    }

    override fun drawRect(x: Float, y: Float, width: Float, height: Float, fillColor: Color4f) {
        nvgBeginPath(vg)
        nvgRect(vg, x, y, width, height)
        nvgFillColor(vg, colour.set(fillColor))
        nvgFill(vg)
    }

    override fun drawText(x: Float, y: Float, rowWidth: Float, fontSize: Float, color: Color4f, text: String) {
        nvgFontSize(vg, fontSize)
        nvgFontFace(vg, defaultFont)
        nvgTextAlign(vg, NVG_ALIGN_LEFT or NVG_ALIGN_TOP)
        nvgFillColor(vg, colour.set(color))
        nvgTextBox(vg, x, y, rowWidth, text)
    }

    override fun measureText(rowWidth: Float, fontSize: Float, text: String): Vector2f {
        nvgFontSize(vg, fontSize)
        nvgFontFace(vg, defaultFont)
        nvgTextAlign(vg, NVG_ALIGN_LEFT or NVG_ALIGN_TOP)
        val bounds = FloatArray(4)
        nvgTextBoxBounds(vg, 0f, 0f, rowWidth, text, bounds)
        return Vector2f(bounds[2], bounds[3])
    }

    override suspend fun initResources() {
        val font = Loader.loadResource("fonts/OpenSans-Regular.ttf") as ByteBuffer
        nvgCreateFontMem(vg, defaultFont, font, 0)
    }

    override fun drawImage(x: Float, y: Float, width: Float, height: Float, image: Image2DData) {
        var imageHandle = loadedImages.getOrPut(image) {
            val openGLTexture = Texture2D(image)
            val handle = nvglCreateImageFromHandle(vg, openGLTexture.texId, image.width, image.height, 0)
            NVGPaint.create().updateImage(width, height, handle)
        }

        if (width.toInt() != imageHandle.width || height.toInt() != imageHandle.height) {
            imageHandle = imageHandle.pattern.updateImage(width, height, imageHandle.handle)
            loadedImages[image] = imageHandle
        }

        nvgBeginPath(vg)
        nvgTranslate(vg, x, y)
        nvgRect(vg, 0f, 0f, width, height)
        nvgFillPaint(vg, imageHandle.pattern)
        nvgFill(vg)
        nvgResetTransform(vg)
    }

    private fun NVGPaint.updateImage(width: Float, height: Float, handle: Int): LoadedImage {
        val updatedPaint =
            nvgImagePattern(vg, 0f, 0f, width, height, 0f, handle, 1f, this)
        return LoadedImage(handle, updatedPaint, width.toInt(), height.toInt())
    }

    /**
     * Copies color data from Color4f to NVGColor
     */
    private fun NVGColor.set(color4f: Color4f): NVGColor {
        r(color4f.red).g(color4f.green).b(color4f.blue).a(color4f.alpha)
        return this
    }
}