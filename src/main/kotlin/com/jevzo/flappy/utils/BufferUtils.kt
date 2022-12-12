package com.jevzo.flappy.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

class BufferUtils {

    companion object {
        fun createByteBuffer(byteArray: ByteArray): ByteBuffer {
            val byteBuffer = ByteBuffer.allocateDirect(byteArray.size).order(ByteOrder.nativeOrder())
            byteBuffer.put(byteArray).flip()

            return byteBuffer
        }

        fun createFloatBuffer(floatArray: FloatArray): FloatBuffer {
            val floatBuffer = ByteBuffer.allocateDirect(floatArray.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()

            floatBuffer.put(floatArray).flip()

            return floatBuffer
        }

        fun createIntBuffer(intArray: IntArray): IntBuffer {
            val intBuffer = ByteBuffer.allocateDirect(intArray.size * 4)
                .order(ByteOrder.nativeOrder()).asIntBuffer()

            intBuffer.put(intArray).flip()

            return intBuffer
        }
    }
}