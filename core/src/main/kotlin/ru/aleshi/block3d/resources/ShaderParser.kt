package ru.aleshi.block3d.resources

import com.charleskorn.kaml.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.aleshi.block3d.data.ShaderData
import java.io.InputStream
import java.io.OutputStream

/**
 * Shader program parser. Produces ShaderData instance
 * @see ru.aleshi.block3d.data.ShaderData
 */
class ShaderParser : IParser, IComposer {

    override suspend fun parse(inputStream: InputStream): Any = withContext(Dispatchers.IO) {
        Yaml.default.decodeFromString(ShaderData.serializer(), inputStream.bufferedReader().readText())
    }

    override fun compose(outputStream: OutputStream, obj: Any) {
        val string = Yaml.default.encodeToString(ShaderData.serializer(), obj as ShaderData)
        outputStream.bufferedWriter().apply {
            write(string)
            flush()
        }
    }
}