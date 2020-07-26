package ru.aleshi.block3d.lights

import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.types.Vector3f
import ru.aleshi.block3d.types.Vector4f

/**
 * A directional light. Models a global light source, like the sun.
 */
class DirectionalLight : LightSource() {

    override val viewModelPosition: Vector3f
        get() {
            // Simplification for: transform.matrix() * Vector4f.forward
            val direction = transform.matrix().array().let { mat -> Vector4f(mat[8], mat[9], mat[10], 0f) }

            return (Camera.active.viewMatrix * direction).toVector3()
        }

}