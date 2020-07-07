package ru.aleshi.block3d.resources

import java.io.IOException
import java.io.InputStream

/**
 * Common interface for all parsers implementations
 */
interface IParser {

    /**
     * Parses some data from input stream and returns newly created object depending of parser type.
     */
    @Throws(IOException::class)
    fun parse(inputStream: InputStream): Any

}