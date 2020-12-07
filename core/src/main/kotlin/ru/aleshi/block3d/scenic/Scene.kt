package ru.aleshi.block3d.scenic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.lwjgl.opengl.GL11.*
import ru.aleshi.block3d.World
import ru.aleshi.block3d.renderer.AbstractRenderer
import ru.aleshi.block3d.renderer.SimpleForwardRenderer
import ru.aleshi.block3d.resources.ResourceList

/**
 * Scene is an entity that holds all objects, such as cameras, meshes, light sources, GUI elements, and others.
 */
abstract class Scene {

    private var sceneRenderer: AbstractRenderer = SimpleForwardRenderer()

    private var started: Boolean = false

    /**
     * Sets the current scene renderer. Default scene renderer is [SimpleForwardRenderer]
     */
    fun setRenderer(renderer: AbstractRenderer) {
        sceneRenderer = renderer
    }

    /**
     * Gets the current scene renderer.
     */
    val renderer: AbstractRenderer
        get() = sceneRenderer

    /**
     * The scene resource list.
     */
    val resources: ResourceList = ResourceList(ResourceList.default)

    /**
     * Scene background
     */
    var background: Background = SolidColorBackground()
        set(value) {
            field = value
            field.onApply()
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
        resetState()
        glClearDepth(1.0)
        glDepthRange(0.0, 1.0)
        glDepthMask(true)
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST)
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)

        // Apply default values by calling setters
        background = background

        // Enable culling back faces
        cullFace = true

        // Add default camera to the scene
        add(Camera.active)

        started = true

        // Signal modules that scene has started
        for (module in World.current.modules) {
            module.onSceneStarted(this)
        }
    }

    fun resetState() {
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_STENCIL_TEST)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
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

        sceneRenderer.render()

        background.onSceneDrawn()
    }

    /**
     * Called when scene should be stopped
     */
    open fun stop() {
        if (started) {
            // Signal modules that scene has stopped
            for (module in World.current.modules) {
                module.onSceneFinished(this)
            }

            resources.dispose()
            sceneJob.cancel()
        }

        started = false
    }

    /**
     * Adds a new object to the scene. Since the object added to the scene as root, it will updates.
     * If an object already exists in the scene it will be ignored.
     * @param parent if `null` object will be added as root object of the scene. Otherwise it will updates
     * since parent object added.
     */
    fun add(sceneObject: SceneObject, parent: TransformableObject? = null) {
        if (parent == null)
            sceneObjects.add(sceneObject).apply {
                if (this) sceneObject.create()
            }
        else {
            if (sceneObject is TransformableObject)
                sceneObject.parent = parent
            sceneObject.create()
        }
    }

    /**
     * Removes object from the scene
     */
    fun remove(sceneObject: SceneObject) {
        if (sceneObjects.remove(sceneObject))
            sceneObject.destroy()
    }

    internal fun attachToRenderer(meshObject: MeshObject) = sceneRenderer.attachToRenderer(meshObject)

    internal fun detachFromRenderer(meshObject: MeshObject) = sceneRenderer.detachFromRenderer(meshObject)

}