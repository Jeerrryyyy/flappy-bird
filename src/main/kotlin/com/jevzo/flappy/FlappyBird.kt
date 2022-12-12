package com.jevzo.flappy

import com.jevzo.flappy.game.Level
import com.jevzo.flappy.keyboard.KeyboardCallback
import com.jevzo.flappy.shader.Shader
import com.jevzo.flappy.utils.Matrix4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*

class FlappyBird {

    private val keyboardCallback = KeyboardCallback()

    private var level: Level? = null

    private var window: Long = 0

    private var title: String = "Flappy Bird"
    private var width: Int = 1280
    private var height: Int = 720

    fun start() {
        this.initGl()
        this.resetColor()
        this.loadTextures()
        this.loadShaders()

        var lastTime = System.nanoTime()
        var timer = System.currentTimeMillis()

        val nanoSeconds = 1000000000.0 / 60.0

        var delta = 0.0
        var updates = 0
        var frames = 0

        while (!GLFW.glfwWindowShouldClose(window)) {
            val now = System.nanoTime()
            delta += (now - lastTime) / nanoSeconds

            lastTime = now

            if (delta >= 1) {
                this.update()

                updates++
                delta--
            }

            this.render()

            frames++

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000

                println("Updates: $updates, Frames: $frames")
                updates = 0
                frames = 0
            }
        }
    }

    fun stop() {
        glfwDestroyWindow(window)
        glfwTerminate()
    }

    private fun initGl() {
        if (!GLFW.glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
        window = GLFW.glfwCreateWindow(width, height, title, 0, 0)

        if (window == 0L) {
            throw IllegalStateException("Failed to create the GLFW window")
        }

        GLFW.glfwSetKeyCallback(window, keyboardCallback)

        GLFW.glfwMakeContextCurrent(window)
        GL.createCapabilities()

        glEnable(GL_DEPTH_TEST)
        glActiveTexture(GL_TEXTURE1)
        println("OpenGL version: " + glGetString(GL_VERSION))

        level = Level()
    }

    private fun resetColor() {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    private fun loadTextures() {
        val vertexArray = glGenVertexArrays()
        glBindVertexArray(vertexArray)
    }

    private fun loadShaders() {
        val prMatrix = Matrix4f.orthographic(
            -10.0f,
            10.0f,
            -10.0f * 9.0f / 16.0f,
            10.0f * 9.0f / 16.0f,
            -1.0f,
            1.0f
        )

        val backgroundShader = Shader.background()
        backgroundShader.setUniformMatrix4f("pr_matrix", prMatrix)
        backgroundShader.setUniform1i("tex", 1)

        val birdShader = Shader.bird()
        birdShader.setUniformMatrix4f("pr_matrix", prMatrix)
        birdShader.setUniform1i("tex", 1)

        val pipeShader = Shader.pipe()
        pipeShader.setUniformMatrix4f("pr_matrix", prMatrix)
        pipeShader.setUniform1i("tex", 1)
    }

    private fun update() {
        level?.update()

        GLFW.glfwPollEvents()
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // CUSTOM RENDER START
        level?.render()

        val error = glGetError()
        if (error != GL_NO_ERROR) {
            println("OpenGL error: $error")
        }
        // CUSTOM RENDER END

        GLFW.glfwSwapBuffers(window)
    }
}
