package ru.aleshi.block3d.types

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class QuaternionTest {

    @Test
    fun `test quaternion multiplication`() {
        val a = Quaternion(7f, 4f, 5f, 9f)
        val b = Quaternion(1f, 2f, 3f, 4f)
        val product = a * b
        Assertions.assertEquals(Quaternion(39f, 18f, 57f, 6f), product)
    }
}