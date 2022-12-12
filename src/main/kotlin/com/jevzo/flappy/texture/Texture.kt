package com.jevzo.flappy.texture

import com.jevzo.flappy.utils.BufferUtils
import org.lwjgl.opengl.GL20.*
import java.io.FileInputStream
import javax.imageio.ImageIO


class Texture(path: String) {

    private var id: Int = 0

    var width: Int = 0
    private var height: Int = 0

    init {
        id = this.load(path)
    }

    private fun load(path: String): Int {
        val image = ImageIO.read(FileInputStream(path))

        width = image.width
        height = image.height

        val pixels = IntArray(width * height)

        image.getRGB(0, 0, width, height, pixels, 0, width)

        val data = IntArray(width * height)
        for (i in 0 until width * height) {
            val r = pixels[i] and 0xff0000 shr 16
            val g = pixels[i] and 0xff00 shr 8
            val b = pixels[i] and 0xff
            val a = pixels[i] and -0x1000000 shr 24

            data[i] = a shl 24 or (b shl 16) or (g shl 8) or r
        }

        val result = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, result)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA,
            width,
            height,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            BufferUtils.createIntBuffer(data)
        )

        glBindTexture(GL_TEXTURE_2D, 0)

        return result
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}