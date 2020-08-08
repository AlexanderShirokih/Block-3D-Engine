package ru.aleshi.block3d.scenic

/**
 * Base class for all kind of objects, such as cameras, renderable objects, etc
 */
abstract class SceneObject {

    private var created: Boolean = false

    internal open fun update() {
        if (!created)
            return
        onUpdate()
    }

    internal open fun create() {
        if (!created) {
            onCreate()
            created = true
        }
    }

    internal open fun destroy() {
        if (created) {
            onDestroy()
            created = false
        }
    }

    /**
     * Called once when at was added to scene
     */
    abstract fun onCreate()

    /**
     * Called every time when object should be updated
     */
    abstract fun onUpdate()

    /**
     * Called every time after [onUpdate] state was completed for all objects
     */
    abstract fun onPostUpdate()

    /**
     * Called before object should be deleted
     */
    abstract fun onDestroy()

}