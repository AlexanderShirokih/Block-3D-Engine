package ru.aleshi.block3d.resources

import kotlinx.serialization.json.Json
import ru.aleshi.block3d.data.ImageCubeSource
import ru.aleshi.block3d.internal.data.Image2DData
import ru.aleshi.block3d.internal.data.ImageCubeData
import java.io.InputStream

/**
 * Loads JSON file containing info about parser. Produces [ImageCubeSource] instance
 * @see ru.aleshi.block3d.data.ShaderData
 */
class CubeMapParser : IParser {

    override suspend fun parse(inputStream: InputStream): Any {
        val data = inputStream.bufferedReader().readText()
        return Json.Default
            .decodeFromString(ImageCubeSource.serializer(), data)
            .faceImagePaths
            .mapValues { Loader.loadResource(it.value) as Image2DData }
            .run { ImageCubeData(this) }
    }

}