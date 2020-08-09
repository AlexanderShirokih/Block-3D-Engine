package ru.aleshi.block3d.debug

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30C
import org.lwjgl.system.MemoryStack
import ru.aleshi.block3d.scenic.Camera
import ru.aleshi.block3d.Defaults
import ru.aleshi.block3d.Material
import ru.aleshi.block3d.renderer.SimpleForwardRenderer

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
    var drawMode: DrawMode = DrawMode.SOLID

    /**
     * Adds new [debugObject] to debug objects queue.
     */
    fun addDebugEntity(debugObject: DebugObjectDrawer) {
        debugElements.add(debugObject)
    }

    override fun render() {
        GL30C.glPolygonMode(GL30C.GL_FRONT_AND_BACK, drawMode.glValue)

        super.render()

        if (debugElements.isNotEmpty()) {
            val shader = debugMaterial.shader
            shader.bind()

            MemoryStack.stackPush().use { stack -> debugMaterial.attach(stack.mallocFloat(16)) }

            if (drawMode != DrawMode.SOLID)
                GL30C.glPolygonMode(GL30C.GL_FRONT_AND_BACK, DrawMode.SOLID.glValue)


            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY)
            for (el in debugElements) {
                debugMaterial.attach("color", el.color)
                el.draw()
            }
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY)

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