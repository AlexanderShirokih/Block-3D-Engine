package ru.aleshi.block3d.types

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Matrix4fTest {

    @Test
    fun `identity() makes matrix identity`() {
        val matrix = Matrix4f().identity()

        Assertions.assertArrayEquals(
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

        Assertions.assertArrayEquals(
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
        Assertions.assertArrayEquals(target, matrix4f.array())
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

        Assertions.assertEquals(Matrix4f(a), Matrix4f(a))
        Assertions.assertNotEquals(Matrix4f(a), Matrix4f(b))
    }

}