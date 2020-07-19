package ru.aleshi.block3d.lights

import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.TransformableObject
import ru.aleshi.block3d.shader.LinkableProperty
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Vector3f

/**
 * Common class for light sources
 */
abstract class LightSource : TransformableObject() {

    /**
     * Light color
     */
    @LinkableProperty
    var color: Color4f = Color4f.white

    /**
     * Light intensity coefficient
     */
    @LinkableProperty
    var intensity: Float = 0f

    /**
     * Light position in view-model space
     */
    @LinkableProperty("vmPosition")
    val viewModelPosition: Vector3f
        get() = (Camera.active.viewMatrix * transform.matrix()).position()
}