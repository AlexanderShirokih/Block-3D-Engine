package ru.aleshi.block3d

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.lwjgl.opengl.GL11.*
import ru.aleshi.block3d.types.Color4f

/**
 * Scene is an entity that holds all objects, such as cameras, meshes, GUI elements, and others.
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

    companion object {
        /**
         * Returns current scene of current world
         */
        val current: Scene
            get() = World.current.currentScene
    }

    private val sceneJob: Job by lazy { Job(World.current.worldJob) }

    /**
     * Per-scene coroutine scope.
     */
    val sceneScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main + sceneJob) }

    private val sceneObjects = mutableSetOf<SceneObject>()

    /**
     * The list of objects that currently present of the scene
     */
    var objects: Set<SceneObject> = sceneObjects

    /**
     * Switches blending state. Disables by default
     */
    var blending: Boolean = false
        set(value) {
            if (value != field) {
                if (value)
                    glEnable(GL_BLEND)
                else
                    glDisable(GL_BLEND)
            }
            field = value
        }

    /**
     * Switches back face culling. Enabled by default.
     */
    var cullFace: Boolean = false
        set(value) {
            if (value != field) {
                if (value)
                    glEnable(GL_CULL_FACE)
                else
                    glDisable(GL_CULL_FACE)
                field = value
            }
        }

    /**
     * Should be called once to initialize scene. Super function should be called!
     */
    open fun create() {
        // Apply default settings
        glEnable(GL_DEPTH_TEST)
        glClearDepth(1.0)
        glDepthFunc(GL_LESS)
        glDepthRange(0.0, 1.0)
        glDepthMask(true)
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST)
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        // Apply default values by calling setters
        background = background

        // Enable culling back faces
        cullFace = true

        // Add default camera to the scene
        addObject(Camera.active)
    }

    /**
     * Called when window size changed
     */
    open fun resize(width: Int, height: Int) {
        // Update viewport data
        glViewport(0, 0, width, height)
        Camera.active.onResize(width.toFloat(), height.toFloat())
    }

    /**
     * Called on each frame to update the scene
     */

    open fun update() {
        sceneObjects.forEach { it.update() }
        sceneObjects.forEach { it.postUpdate() }
    }

    /**
     * Called when scene should be stopped
     */
    open fun stop() {
        sceneJob.cancel()
    }

    /**
     * Adds a new root object to the scene. Since the object added to the scene it will updates.
     * If an object already exists in the scene it will be ignored.
     * Another way to add object to the scene is to set its parent to `null`.
     * @return `true` if the object has been added, `false` if the object is already contained in the scene.
     */
    fun addObject(sceneObject: SceneObject) =
        sceneObjects.add(sceneObject).apply {
            if (this) sceneObject.create()
        }

    /**
     * Removes object from the scene
     */
    fun removeObject(sceneObject: SceneObject) {
        if (sceneObjects.remove(sceneObject))
            sceneObject.destroy()
    }
}