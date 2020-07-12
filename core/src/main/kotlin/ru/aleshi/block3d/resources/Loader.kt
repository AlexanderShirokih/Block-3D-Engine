package ru.aleshi.block3d.resources

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.RuntimeException

/**
 * Common gate for parsing all supported resource types
 */
@Suppress("BlockingMethodInNonBlockingContext")
object Loader {

    private val installedParsers = mutableMapOf<String, Class<out IParser>>(
        "shc" to ShaderParser::class.java,
        "png" to PNGImageParser::class.java,
        "obj" to WavefrontObjectParser::class.java
    )

    private val installedComposers = mutableMapOf<String, Class<out IComposer>>(
        "shc" to ShaderParser::class.java
    )

    private fun getParser(extension: String): IParser {
        val parserClass = installedParsers[extension]
            ?: throw RuntimeException("There is no installed parsers for \'$extension\' extension")
        return parserClass.newInstance()
    }

    private fun getComposer(extension: String): IComposer {
        val composerClass = installedComposers[extension]
            ?: throw RuntimeException("There is no installed composers for '$extension' extension")
        return composerClass.newInstance()
    }

    /**
     * Loads resource from resources folder
     */
    suspend fun loadResource(resourceName: String): Any {
        val resource = ClassLoader.getSystemResource(resourceName)
        val ext = resource.file.substringAfterLast('.', "")
        val stream = withContext(Dispatchers.IO) { resource.openStream() }
        return loadFromInputStream(ext, stream)
    }

    /**
     * Loads resource using parser depending of file extension.
     * @see loadFromInputStream
     */
    suspend fun load(file: File): Any = loadFromInputStream(file.extension, FileInputStream(file))

    /**
     * Loads object of type [type] from input stream. After reading stream will be closed.
     * @return loaded object of type dependent of object type
     * @throws RuntimeException when parser for this type was not found
     * @throws java.io.IOException if something went wrong while reading from input stream
     * @throws Exception This method can also throw any other exception types depending of parser
     */
    suspend fun loadFromInputStream(type: String, inputStream: InputStream): Any =
        inputStream.use { getParser(type).parse(it) }

    /**
     * Stores object [obj] to file [file] using composer depending of file extension.
     * @see storeToOutputStream
     */
    fun store(obj: Any, file: File) = storeToOutputStream(obj, file.extension, FileOutputStream(file))

    /**
     * Stores object of type [type] to output stream. After writing the object stream will be closed.
     * @throws RuntimeException when composer for this type was not found
     * @throws java.io.IOException if something went wrong while writing to output stream
     * @throws Exception This method can also throw any other exception types depending of composer
     */
    fun storeToOutputStream(obj: Any, type: String, outputStream: OutputStream) {
        outputStream.use {
            getComposer(type).compose(it, obj)
        }
    }

    /**
     * Installs parser with given extension.
     * @return `false` if parser for this extension already exists, `true` if parser was installed successfully
     */
    fun installParser(extension: String, parserClass: Class<out IParser>): Boolean =
        installedParsers.putIfAbsent(extension, parserClass) != null

    /**
     * Removes parser with given extension.
     * @return `true` is parser was removed, `false` is parser was not found.
     */
    fun removeParser(extension: String) =
        installedParsers.remove(extension) != null

    /**
     * Installs composer with given extension.
     * @return `false` if composer for this extension already exists, `true` if composer was installed successfully
     */
    fun installComposer(extension: String, composerClass: Class<out IComposer>): Boolean =
        installedComposers.putIfAbsent(extension, composerClass) != null

    /**
     * Removes composer with given extension.
     * @return `true` is composer was removed, `false` is composer was not found.
     */
    fun removeComposer(extension: String) =
        installedComposers.remove(extension) != null
}