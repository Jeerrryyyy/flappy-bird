package com.jevzo.flappy.game

import com.jevzo.flappy.texture.Texture
import com.jevzo.flappy.utils.Matrix4f
import com.jevzo.flappy.utils.Vector3f
import com.jevzo.flappy.utils.VertexArray

class Pipe(
    val x: Float,
    val y: Float
) {

    val position: Vector3f = Vector3f()
    var matrix: Matrix4f? = null

    init {
        position.x = x
        position.y = y

        matrix = Matrix4f.translate(position)
    }

    companion object {
        var pipeMesh: VertexArray? = null
        var pipeTexture: Texture? = null

        val width: Float = 1.5f
        val height: Float = 8.0f

        fun initMeshAndTextures() {
            val vertices = floatArrayOf(
                0.0f, 0.0f, 0.1f,
                0.0f, height, 0.1f,
                width, height, 0.1f,
                width, 0.0f, 0.1f,
            )

            val indices = byteArrayOf(
                0, 1, 2,
                2, 3, 0
            )

            val textureCoordinates = floatArrayOf(
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
            )

            pipeMesh = VertexArray(vertices, indices, textureCoordinates)
            pipeTexture = Texture("texture/obstacle.png")
        }
    }
}