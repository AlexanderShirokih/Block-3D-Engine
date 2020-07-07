package ru.aleshi.block3d.resources

import com.charleskorn.kaml.Yaml
import ru.aleshi.block3d.data.ShaderData
import java.io.InputStream
import java.io.OutputStream

/**
 * Shader program parser. Produces ShaderData instance
 * @see ru.aleshi.block3d.data.ShaderData
 */
class ShaderParser : IParser, IComposer {

    override fun parse(inputStream: InputStream): Any =
        Yaml.default.parse(ShaderData.serializer(), inputStream.bufferedReader().readText())

    override fun compose(outputStream: OutputStream, obj: Any) {
        val string = Yaml.default.stringify(ShaderData.serializer(), obj as ShaderData)
        println(string)
        outputStream.bufferedWriter().apply {
            write(string)
            flush()
        }
    }
}