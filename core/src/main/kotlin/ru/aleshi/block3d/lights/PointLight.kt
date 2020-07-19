package ru.aleshi.block3d.lights

import ru.aleshi.block3d.shader.LinkableProperty

/**
 * A point light source.
 */
class PointLight : LightSource() {

    @LinkableProperty
    var attenuation: Float = 1f

}