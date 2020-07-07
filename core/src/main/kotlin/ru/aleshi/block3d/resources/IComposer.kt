package ru.aleshi.block3d.resources

import java.io.IOException
import java.io.OutputStream

/**
 * Common interface for all composers implementations
 */
interface IComposer {

    /**
     * Composes some object [obj] depending of composer type into it's serialized representation
     * and stores it to output stream [outputStream].
     */
    @Throws(IOException::class)
    fun compose(outputStream: OutputStream, obj: Any)

}