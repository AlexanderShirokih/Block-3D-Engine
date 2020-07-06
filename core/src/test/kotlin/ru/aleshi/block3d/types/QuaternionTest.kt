package ru.aleshi.block3d.types

import org.junit.jupiter.api.Test

class QuaternionTest {

    @Test
    fun `test quaternion multiplication`() {
        val a = Quaternion(7f, 4f, 5f, 9f)
        val b = Quaternion(1f, 2f, 3f, 4f)
        val product = a * b
        println(product)
    }
}