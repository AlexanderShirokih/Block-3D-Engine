package ru.aleshi.block3d.ui

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.system.MemoryUtil.NULL
import ru.aleshi.block3d.internal.WindowConfig
import ru.aleshi.block3d.types.Color4f

/**
 * Creates UI rendering context using NanoVG library
 */
class NanoVGRenderContext : UIRenderContext {

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

    /**
     * Copies color data from Color4f to NVGColor
     */
    private fun NVGColor.set(color4f: Color4f): NVGColor {
        r(color4f.red).g(color4f.green).b(color4f.blue).a(color4f.alpha)
        return this
    }
}