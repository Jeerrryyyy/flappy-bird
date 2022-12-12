package com.jevzo.flappy.game

import com.jevzo.flappy.keyboard.KeyboardCallback
import com.jevzo.flappy.shader.Shader
import com.jevzo.flappy.texture.Texture
import com.jevzo.flappy.utils.Matrix4f
import com.jevzo.flappy.utils.Vector3f
import com.jevzo.flappy.utils.VertexArray
import org.lwjgl.glfw.GLFW

class Bird {

    val birdSize = 1.0f
    private var birdMesh: VertexArray
    private var birdTexture: Texture

    var position: Vector3f = Vector3f()

    private var rotation: Float = 0.0f
    private var delta: Float = 0.0f

    init {
        val vertices = floatArrayOf(
            -birdSize / 2.0f, -birdSize / 2.0f, 0.2f,
            -birdSize / 2.0f, birdSize / 2.0f, 0.2f,
            birdSize / 2.0f, birdSize / 2.0f, 0.2f,
            birdSize / 2.0f, -birdSize / 2.0f, 0.2f
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

        birdMesh = VertexArray(vertices, indices, textureCoordinates)
        birdTexture = Texture("texture/flappy.png")
    }

    fun update() {
        position.y -= delta

        when {
            KeyboardCallback.isKeyDown(GLFW.GLFW_KEY_SPACE) -> {
                delta = -0.15f
            }

            else -> {
                delta += 0.01f
            }
        }

        rotation = -delta * 90.0f
    }

    fun render() {
        Shader.bird().enable()
        Shader.bird().setUniformMatrix4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation)))

        birdTexture.bind()
        birdMesh.render()
        birdTexture.unbind()

        Shader.bird().disable()
    }
}