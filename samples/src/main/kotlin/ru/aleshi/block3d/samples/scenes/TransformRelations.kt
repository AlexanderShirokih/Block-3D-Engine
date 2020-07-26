package ru.aleshi.block3d.samples.scenes

import ru.aleshi.block3d.Camera
import ru.aleshi.block3d.Defaults
import ru.aleshi.block3d.Scene
import ru.aleshi.block3d.TransformableObject
import ru.aleshi.block3d.primitives.Sphere
import ru.aleshi.block3d.types.Color4f
import ru.aleshi.block3d.types.Quaternion
import ru.aleshi.block3d.types.Vector3f

/**
 * This example shows work of relative transformations in object hierarchy
 */
class TransformRelations : Scene() {

    lateinit var sun: TransformableObject
    lateinit var earth: TransformableObject
    lateinit var moon: TransformableObject

    private val rotation = Quaternion.fromAxisAngle(Vector3f.up, 0.2f)

    override fun create() {
        super.create()
        background = Color4f.grayscale(0.13f)
        Camera.active.transform.position = Vector3f.forward * 60f
        Camera.active.transform.rotation = Quaternion.fromAxisAngle(Vector3f.right, -35f)

        sun = Sphere(Defaults.MATERIAL_UNLIT).apply {
            addObject(this)
            transform.scale = Vector3f(10f)
            material.setProperty("color", Color4f(1f, 1f, 0f))
        }

        earth = Sphere(Defaults.MATERIAL_UNLIT).apply {
            addObject(this, sun)
            transform.scale = Vector3f(.5f)
            transform.position = Vector3f.right * 3f
            material.setProperty("color", Color4f(0f, 0f, 1f))
        }

        moon = Sphere(Defaults.MATERIAL_UNLIT).apply {
            addObject(this, earth)
            transform.scale = Vector3f(0.3f)
            transform.position = Vector3f.forward * 1.5f
            material.setProperty("color", Color4f.grayscale(0.7f))
        }
    }

    override fun update() {
        super.update()
        sun.transform.rotation *= rotation
        earth.transform.rotation *= rotation * rotation
    }
}