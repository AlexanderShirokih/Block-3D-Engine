package ru.aleshi.block3d

import org.lwjgl.opengl.GL11.*
import ru.aleshi.block3d.types.Color4f

/**
 * Scene is an entity that holds all objects, such as cameras, meshes, gui elements and other.
 */
abstract class Scene {

    /**
     * Background color for the scene
     */
    var background: Color4f = Color4f.magenta
        set(value) {
            field = value
            glClearColor(background.red, background.green, background.blue, background.alpha)
        }

    /**
     * Should be called once to initialize scene. Super function should be called!
     */
    open fun create() {
        // Apply default settings
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_NORMALIZE)
        glClearDepth(1.0)
        glDepthFunc(GL_LESS)
        glDepthRange(0.0, 1.0)
        glDepthMask(true)
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST)
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)

        // Apply default values by calling setters
        background = background
    }

    /**
     * Called when window size changed
     */
    open fun resize(width: Int, height: Int) {
        //TODO:
    }

    /**
     * Called on each frame to update the scene
     */
    open fun update() = Unit

    /**
     * Called when scene should be stopped
     */
    open fun stop() = Unit
}