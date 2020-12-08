package ru.aleshi.block3d.ui.internal

import ru.aleshi.block3d.internal.data.Image2DData
import ru.aleshi.block3d.resources.Loader

class ImageCache {

    private val cache: MutableMap<String, Image2DData> = mutableMapOf()

    suspend fun getOrLoad(name: String): Image2DData {
        return cache.getOrPut(name) { Loader.loadResource(name) as Image2DData }
    }

    fun cleanup() {
        cache.values.forEach { it.recycle() }
        cache.clear()
    }
}