package ru.aleshi.block3d

import ru.aleshi.block3d.scenic.Scene

/**
 * Common interface to dynamically load new modules
 */
interface Block3DModule {

    /**
     * Called once when window instance is created
     */
    fun onWindowCreated(window: Window)

    /**
     * Called once when window instance is destroyed
     */
    fun onWindowDestroyed()

    /**
     * Called once per frame after scene rendering to update module state
     */
    fun onUpdate(world: World)

    /**
     * Called when on scene start
     */
    fun onSceneStarted(scene: Scene)

    /**
     * Called on scene finished
     */
    fun onSceneFinished(scene: Scene)

}