package ru.aleshi.block3d.debug

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30C
import org.lwjgl.system.MemoryStack
import ru.aleshi.block3d.Defaults
import ru.aleshi.block3d.Material
import ru.aleshi.block3d.debug.DebugRenderer.DrawMode.*
import ru.aleshi.block3d.renderer.SimpleForwardRenderer
import ru.aleshi.block3d.scenic.Camera
import java.nio.FloatBuffer

/**
 * Debugging renderer. Used to drawInstance some debugging elements.
 */
class DebugRenderer : SimpleForwardRenderer() {

    /**
     * Mesh rendering mode.
     * [SOLID] - for solid triangles drawing. [WIREFRAME] - for line drawing. [POINTS] - points drawing.
     */
    enum class DrawMode(internal val glValue: Int) {
        SOLID(GL30C.GL_FILL), WIREFRAME(GL30C.GL_LINE), POINTS(GL30C.GL_POINT)
    }

    private val debugMaterial: Material = Defaults.MATERIAL_UNLIT.copy().apply {
        setProperty("modelViewMatrix", { Camera.active.viewMatrix })
        setProperty("projectionMatrix", { Camera.active.projectionMatrix })
    }

    private val debugElements = mutableListOf<DebugObjectDrawer>()

    /**
     * Current rendering mode
     */
    var drawMode: DrawMode = SOLID

    /**
     * Adds new [debugObject] to debug objects queue.
     */
    fun addDebugEntity(debugObject: DebugObjectDrawer) {
        debugElements.add(debugObject)
    }

    override fun render() {
        GL30C.glPolygonMode(GL30C.GL_FRONT_AND_BACK, drawMode.glValue)

        super.render()

        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(16)

            drawDebugElements(buffer)
        }
    }

    private inline fun withDrawMode(mode: DrawMode, action: () -> Unit) {
        if (drawMode != DrawMode.SOLID)
            GL30C.glPolygonMode(GL30C.GL_FRONT_AND_BACK, mode.glValue)

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY)
        action()
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY)
    }

    private fun drawDebugElements(buffer: FloatBuffer) {
        if (debugElements.isNotEmpty()) {
            val shader = debugMaterial.shader
            shader.bind()
            debugMaterial.attach(buffer)

            withDrawMode(DrawMode.SOLID) {
                for (el in debugElements) {
                    debugMaterial.attach("color", el.color)
                    el.draw()
                }
            }

            shader.unbind()

            debugElements.removeIf {
                val condition = !it.keepInScene
                if (condition)
                    it.dispose()
                condition
            }
        }
    }

    fun clearDebug() {
        debugElements.forEach { it.dispose() }
        debugElements.clear()
    }

}