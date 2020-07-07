package ru.aleshi.block3d

/**
 * Base class for all kind of objects, such as cameras, renderable objects, etc
 */
abstract class SceneObject {

    internal open fun update() {
        onUpdate()
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
     * Called before object should be deleted
     */
    abstract fun onDelete()

}