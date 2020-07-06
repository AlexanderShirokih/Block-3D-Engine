package ru.aleshi.block3d.types

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Vector3fTest {

    @Test
    fun `test vector length`() {
        val len = Vector3f(6f, 8f, 0f).magnitude
        Assertions.assertEquals(10f, len, 0.001f)
    }

    @Test
    fun `test dot product`() {
        val a = Vector3f(1f, 3f, -5f)
        val b = Vector3f(4f, -2f, -1f)

        Assertions.assertEquals(3f, a.dot(b), 0.001f)
    }

    @Test
    fun `test cross product`() {
        val a = Vector3f(3f, -5f, 4f)
        val b = Vector3f(2f, 6f, 5f)
        val product = a.cross(b)

        Assertions.assertEquals(Vector3f(-49f, -7f, 28f), product)
    }
}