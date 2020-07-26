package ru.aleshi.block3d.types

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Matrix4fTest {

    @Test
    fun `identity() makes matrix identity`() {
        val matrix = Matrix4f().identity()

        assertArrayEquals(
            floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
            ),
            matrix.array()
        )
    }

    @Test
    fun `new matrix should be identity`() {
        val matrix = Matrix4f()

        assertArrayEquals(
            floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
            ),
            matrix.array()
        )
    }

    @Test
    fun `set elements works correctly`() {
        val target = floatArrayOf(
            1f, 2f, 3f, 4f,
            2f, 3f, 4f, 1f,
            3f, 4f, 1f, 2f,
            4f, 1f, 2f, 3f
        )
        val targetMatrix = Matrix4f(target)
        val matrix4f = Matrix4f().set(targetMatrix)
        assertArrayEquals(target, matrix4f.array())
    }

    @Test
    fun `equals works correctly`() {
        val a = floatArrayOf(
            1f, 2f, 3f, 4f,
            2f, 3f, 4f, 1f,
            3f, 4f, 1f, 2f,
            4f, 1f, 2f, 3f
        )
        val b = floatArrayOf(
            3f, 2f, 3f, 4f,
            2f, 3f, 4f, 1f,
            3f, 4f, 1f, 2f,
            4f, 1f, 2f, 3f
        )

        assertEquals(Matrix4f(a), Matrix4f(a))
        assertNotEquals(Matrix4f(a), Matrix4f(b))
    }

    @Test
    fun `test multiplication by vector3`() {
        val mat = Matrix4f().translate(10f, 0f, 0f)

        assertEquals(Vector3f(10f, 0f, 0f), mat.position())

        val result = mat * Vector3f(10f, 10f, 10f)

        assertEquals(Vector3f(20f, 10f, 10f), result)
    }

}