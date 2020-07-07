package ru.aleshi.block3d

/**
 * Describes object that should dispose its resource at the end of life
 */
interface IDisposable {

    /**
     * Call to dispose resource
     */
    fun dispose()

}