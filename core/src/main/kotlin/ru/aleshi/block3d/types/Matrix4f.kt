package ru.aleshi.block3d.types

import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

private const val PI_OVER_180 = 0.01745329

/**
 * Describes Matrix 4x4
 */
class Matrix4f(private val matrix: FloatArray) {

    constructor() : this(FloatArray(16)) {
        identity()
    }

    /**
     * Makes identity matrix
     */
    fun identity(): Matrix4f {
        Arrays.fill(matrix, 0f)
        matrix[0] = 1.0f
        matrix[5] = 1.0f
        matrix[10] = 1.0f
        matrix[15] = 1.0f
        return this
    }

    /**
     * Copies data from @param m to this matrix and returns this matrix
     */
    fun set(m: Matrix4f): Matrix4f {
        System.arraycopy(m.matrix, 0, matrix, 0, 16)
        return this
    }

    /**
     * Multiplies two matrices and returns new matrix as result
     */
    operator fun times(m: Matrix4f): Matrix4f {
        val result = Matrix4f()
        multiplyMatrixArrays(matrix, m.matrix, result.matrix)
        return result
    }

    private fun multiplyMatrixArrays(l: FloatArray, r: FloatArray, res: FloatArray) {
        for (i in 0 until 4) {
            res[i] = l[i] * r[0] + l[i + 4] * r[1] + l[i + 8] * r[2] + l[i + 12] * r[3]
            res[i + 4] = l[i] * r[4] + l[i + 4] * r[5] + l[i + 8] * r[6] + l[i + 12] * r[7]
            res[i + 8] = l[i] * r[8] + l[i + 4] * r[9] + l[i + 8] * r[10] + l[i + 12] * r[11]
            res[i + 12] = l[i] * r[12] + l[i + 4] * r[13] + l[i + 8] * r[14] + l[i + 12] * r[15]
        }
    }

    /**
     * Translates matrix to position (x, y, z)
     */
    fun translate(x: Float, y: Float, z: Float): Matrix4f {
        for (i in 0..3) {
            matrix[12 + i] += matrix[i] * x + matrix[4 + i] * y + matrix[8 + i] * z
        }
        return this
    }


    /**
     * Rotates matrix in Euler degrees and returns new matrix as result
     */
    fun rotate(x: Float, y: Float, z: Float): Matrix4f {
        val xRad = x * PI_OVER_180
        val yRad = y * PI_OVER_180
        val zRad = z * PI_OVER_180

        val cx = cos(xRad).toFloat()
        val sx = sin(xRad).toFloat()
        val cy = cos(yRad).toFloat()
        val sy = sin(yRad).toFloat()
        val cz = cos(zRad).toFloat()
        val sz = sin(zRad).toFloat()
        val cxsy = cx * sy
        val sxsy = sx * sy

        val rotationMatrix = FloatArray(16)

        rotationMatrix[0] = cy * cz
        rotationMatrix[1] = -cy * sz
        rotationMatrix[2] = sy
        rotationMatrix[3] = 0.0f

        rotationMatrix[4] = cxsy * cz + cx * sz
        rotationMatrix[5] = -cxsy * sz + cx * cz
        rotationMatrix[6] = -sx * cy
        rotationMatrix[7] = 0.0f

        rotationMatrix[8] = -sxsy * cz + sx * sz
        rotationMatrix[9] = sxsy * sz + sx * cz
        rotationMatrix[10] = cx * cy
        rotationMatrix[11] = 0.0f

        rotationMatrix[12] = 0.0f
        rotationMatrix[13] = 0.0f
        rotationMatrix[14] = 0.0f
        rotationMatrix[15] = 1.0f

        return Matrix4f().also { product -> multiplyMatrixArrays(matrix, rotationMatrix, product.matrix) }
    }

    /**
     * Scales matrix by factor x,y,z and returns this matrix as result
     */
    fun scale(x: Float, y: Float, z: Float): Matrix4f {
        for (i in 0 until 4) {
            matrix[i] *= x
            matrix[i + 4] *= y
            matrix[i + 8] *= z
        }
        return this
    }

    /**
     * Computes an orthographic projection for this matrix.
     */
    fun orto(left: Float, right: Float, top: Float, bottom: Float, near: Float, far: Float): Matrix4f {
        if (left == right) {
            throw  IllegalArgumentException("left == right")
        }
        if (bottom == top) {
            throw  IllegalArgumentException("bottom == top")
        }
        if (near == far) {
            throw  IllegalArgumentException("near == far")
        }

        val rWidth = 1.0f / (right - left)
        val rHeight = 1.0f / (top - bottom)
        val rDepth = 1.0f / (far - near)

        matrix[0] = 2.0f * (rWidth)
        matrix[1] = 0.0f
        matrix[2] = 0.0f
        matrix[3] = 0.0f
        matrix[4] = 0.0f
        matrix[5] = 2.0f * (rHeight)
        matrix[6] = 0.0f
        matrix[7] = 0.0f
        matrix[8] = 0.0f
        matrix[9] = 0.0f
        matrix[10] = -2.0f * (rDepth)
        matrix[11] = 0.0f
        matrix[12] = -(right + left) * rWidth
        matrix[13] = -(top + bottom) * rHeight
        matrix[14] = -(far + near) * rDepth
        matrix[15] = 1.0f

        return this
    }

    /**
     * Computes a perspective projection for this matrix.
     */
    fun perspective(fovY: Float, aspect: Float, zNear: Float, zFar: Float): Matrix4f {
        val f = 1.0f / tan(fovY * PI_OVER_180 * 2f).toFloat()
        val rangeReciprocal = 1.0f / (zNear - zFar)

        matrix[0] = f / aspect
        matrix[1] = 0.0f
        matrix[2] = 0.0f
        matrix[3] = 0.0f

        matrix[4] = 0.0f
        matrix[5] = f
        matrix[6] = 0.0f
        matrix[7] = 0.0f

        matrix[8] = 0.0f
        matrix[9] = 0.0f
        matrix[10] = (zFar + zNear) * rangeReciprocal
        matrix[11] = -1.0f

        matrix[12] = 0.0f
        matrix[13] = 0.0f
        matrix[14] = 2.0f * zFar * zNear * rangeReciprocal
        matrix[15] = 0.0f

        return this
    }

    /**
     * Returns float array describing matrix
     */
    fun array(): FloatArray = matrix

    /**
     * Returns `true` if matrix data equals to other matrix
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return matrix.contentEquals((other as Matrix4f).matrix)
    }

    /**
     * Returns hash code of the matrix data
     */
    override fun hashCode(): Int {
        return matrix.contentHashCode()
    }


}