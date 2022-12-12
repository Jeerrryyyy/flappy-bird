package com.jevzo.flappy.utils

import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin

class Matrix4f {

    val matrix: FloatArray = FloatArray(16)

    fun multiply(matrix4f: Matrix4f): Matrix4f {
        val result = Matrix4f()

        for (y in 0..3) {
            for (x in 0..3) {
                var sum = 0.0f

                for (e in 0..3) {
                    sum += this.matrix[x + e * 4] * matrix4f.matrix[e + y * 4]
                }

                result.matrix[x + y * 4] = sum
            }
        }

        return result
    }

    fun toFloatBuffer(): FloatBuffer {
        return BufferUtils.createFloatBuffer(matrix)
    }

    companion object {
        fun identity(): Matrix4f {
            val result = Matrix4f()

            for (i in 0 until 16) {
                result.matrix[i] = 0.0f
            }

            result.matrix[0 + 0 * 4] = 1.0f
            result.matrix[1 + 1 * 4] = 1.0f
            result.matrix[2 + 2 * 4] = 1.0f
            result.matrix[3 + 3 * 4] = 1.0f

            return result
        }

        fun translate(vector3f: Vector3f): Matrix4f {
            val matrix4f = identity()

            matrix4f.matrix[0 + 3 * 4] = vector3f.x
            matrix4f.matrix[1 + 3 * 4] = vector3f.y
            matrix4f.matrix[2 + 3 * 4] = vector3f.z

            return matrix4f
        }

        fun rotate(angle: Float): Matrix4f {
            val result = identity()
            val radians = Math.toRadians(angle.toDouble()).toFloat()

            val cos = cos(radians.toDouble()).toFloat()
            val sin = sin(radians.toDouble()).toFloat()

            result.matrix[0 + 0 * 4] = cos
            result.matrix[1 + 0 * 4] = sin
            result.matrix[0 + 1 * 4] = -sin
            result.matrix[1 + 1 * 4] = cos

            return result
        }

        fun orthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f {
            val matrix4f = identity()

            matrix4f.matrix[0 + 0 * 4] = 2.0f / (right - left)
            matrix4f.matrix[1 + 1 * 4] = 2.0f / (top - bottom)
            matrix4f.matrix[2 + 2 * 4] = 2.0f / (near - far)

            matrix4f.matrix[0 + 3 * 4] = (left + right) / (left - right)
            matrix4f.matrix[1 + 3 * 4] = (bottom + top) / (bottom - top)
            matrix4f.matrix[2 + 3 * 4] = (far + near) / (far - near)

            return matrix4f
        }
    }
}