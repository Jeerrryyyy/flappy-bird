package com.jevzo.flappy.game

import com.jevzo.flappy.shader.Shader
import com.jevzo.flappy.texture.Texture
import com.jevzo.flappy.utils.Matrix4f
import com.jevzo.flappy.utils.Vector3f
import com.jevzo.flappy.utils.VertexArray
import java.util.*

class Level {

    private val random: Random = Random()

    private var backgroundMesh: VertexArray
    private var backgroundTexture: Texture

    private var horizontalScroll: Int = 0
    private var backgroundLocation: Int = 0
    private var index: Int = 0

    private var offset: Float = 5.0f

    private val bird: Bird

    private val pipes: Array<Pipe?> = arrayOfNulls(10)

    init {
        val vertices = floatArrayOf(
            -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
            -10.0f, 10.0f * 9.0f / 16.0f, 0.0f,
            0.0f, 10.0f * 9.0f / 16.0f, 0.0f,
            0.0f, -10.0f * 9.0f / 16.0f, 0.0f
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

        backgroundMesh = VertexArray(vertices, indices, textureCoordinates)
        backgroundTexture = Texture("texture/background.png")

        bird = Bird()

        this.createPipes()
    }

    fun update() {
        horizontalScroll--

        if (-horizontalScroll % 335 == 0) {
            backgroundLocation++
        }

        if (-horizontalScroll > 250 && -horizontalScroll % 120 == 0) {
            this.updatePipes()
        }

        bird.update()
    }

    fun render() {
        backgroundTexture.bind()
        Shader.background().enable()

        backgroundMesh.bind()
        for (i in backgroundLocation until backgroundLocation + 4) {
            Shader.background().setUniformMatrix4f(
                "vw_matrix",
                Matrix4f.translate(Vector3f(i * 10.0f + horizontalScroll * 0.03f, 0.0f, 0.0f))
            )
            backgroundMesh.draw()
        }

        Shader.background().disable()
        backgroundTexture.unbind()

        bird.render()

        this.renderPipes()
    }

    private fun createPipes() {
        Pipe.initMeshAndTextures()

        for (i in 0 until 10 step 2) {
            pipes[i] = Pipe(offset + index * 3.0f, random.nextFloat() * 4.0f)
            pipes[i + 1] = Pipe(pipes[i]!!.x, pipes[i]!!.y - 11.5f)
            index += 2
        }
    }

    private fun updatePipes() {
        pipes[index % 10] = Pipe(offset + index * 3.0f, random.nextFloat() * 4.0f)
        pipes[(index + 1) % 10] = Pipe(pipes[index % 10]!!.x, pipes[index % 10]!!.y - 11.5f)
        index += 2
    }

    private fun renderPipes() {
        Shader.pipe().enable()
        Shader.pipe().setUniformMatrix4f(
            "vw_matrix",
            Matrix4f.translate(Vector3f(horizontalScroll * 0.05f, 0.0f, 0.0f))
        )

        Pipe.pipeTexture?.bind()
        Pipe.pipeMesh?.bind()

        for (i in 0 until 10) {
            Shader.pipe().setUniformMatrix4f("ml_matrix", pipes[i]!!.matrix!!)
            Shader.pipe().setUniform1i("top", if (i % 2 == 0) 1 else 0)
            Pipe.pipeMesh?.draw()
        }

        Pipe.pipeTexture?.unbind()
        Pipe.pipeMesh?.unbind()
        Shader.pipe().disable()
    }

    private fun isColliding(): Boolean {
        for (i in 0 until 10) {
            val birdX = -horizontalScroll * 0.05f
            val birdY = bird.position.y

            val pipeX = pipes[i]!!.x
            val pipeY = pipes[i]!!.y

            val birdX0: Float = birdX - bird.birdSize / 2.0f
            val birdX1: Float = birdX + bird.birdSize / 2.0f
            val birdY0: Float = birdY - bird.birdSize / 2.0f
            val birdY1: Float = birdY + bird.birdSize / 2.0f

            val pipeX1 = pipeX + Pipe.width
            val pipeY1 = pipeY + Pipe.height

            if (birdX1 > pipeX && birdX0 < pipeX1) {
                if (birdY1 > pipeY && birdY0 < pipeY1) {
                    return true
                }
            }
        }
        return false
    }
}