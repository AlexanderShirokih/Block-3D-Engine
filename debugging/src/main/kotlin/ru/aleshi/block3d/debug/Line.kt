package ru.aleshi.block3d.debug

import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil
import ru.aleshi.block3d.scenic.Scene
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector3f
import java.nio.Buffer
import java.nio.FloatBuffer

/**
 * Debugging class, used to draw lines of two lines.
 */
class Line {

    internal class LineObject(
        position: Vector3f, direction: Vector3f, color: Color4f, keepInScene: Boolean
    ) : DebugObjectDrawer(color, keepInScene) {

        private val buffer: FloatBuffer = MemoryUtil.memAllocFloat(3 * 2).apply {
            put(position.x).put(position.y).put(position.z)
                .put(position.x + direction.x)
                .put(position.y + direction.y)
                .put(position.z + direction.z)
            (this as Buffer).flip()
        }

        override fun draw() {
            glVertexPointer(3, GL_FLOAT, 0, buffer)
            glDrawArrays(GL_LINES, 0, 2)
        }

        override fun dispose() {
            MemoryUtil.memFree(buffer)
        }

    }

    companion object {
        /**
         * Draws line from [position] to [position]+[direction] with [color].
         * If [keepInScene] == `true` this line will draws until [DebugRenderer.clearDebug] called.
         */
        fun draw(
            position: Vector3f,
            direction: Vector3f,
            color: Color4f = Color4f.white,
            keepInScene: Boolean = false
        ) {
            val debugRenderer = Scene.current.renderer

            if (debugRenderer !is DebugRenderer) throw DebuggerRendererIsNotSetException()
            debugRenderer.addDebugEntity(LineObject(position, direction, color, keepInScene))
        }
    }
}