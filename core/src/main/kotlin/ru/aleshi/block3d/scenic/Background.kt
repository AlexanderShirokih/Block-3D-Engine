package ru.aleshi.block3d.scenic

/**
 * [Background] is used to fill scene background using solid color or skyboxes
 */
interface Background {

    /**
     * Called when background is applied to the scene
     */
    fun onApply()

    /**
     * Called at every frame after the scene drawn
     */
    fun onSceneDrawn()

}