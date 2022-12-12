package com.jevzo.flappy.utils

import org.lwjgl.opengl.GL30.*

class VertexArray(
    vertices: FloatArray,
    indices: ByteArray,
    textureCoordinates: FloatArray
) {
    private var vertexArray: Int = glGenVertexArrays()
    private var vertexBuffer: Int
    private var indicesBuffer: Int
    private var textureCoordinatesBuffer: Int

    private var count: Int = indices.size

    init {
        glBindVertexArray(vertexArray)

        vertexBuffer = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer)
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW)
        glVertexAttribPointer(ShaderConstants.VERTEX_ATTRIBUTE, 3, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(ShaderConstants.VERTEX_ATTRIBUTE)

        textureCoordinatesBuffer = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, textureCoordinatesBuffer)
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW)
        glVertexAttribPointer(ShaderConstants.TEXTURE_COORDINATES_ATTRIBUTE, 2, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(ShaderConstants.TEXTURE_COORDINATES_ATTRIBUTE)

        indicesBuffer = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW)


        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun bind() {
        glBindVertexArray(vertexArray)

        if (indicesBuffer > 0)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer)
    }

    fun unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun draw() {
        if (indicesBuffer > 0) {
            glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0)
            return
        }
        glDrawArrays(GL_TRIANGLES, 0, count)
    }

    fun render() {
        this.bind()
        this.draw()
    }
}