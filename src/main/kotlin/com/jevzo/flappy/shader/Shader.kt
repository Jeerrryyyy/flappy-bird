package com.jevzo.flappy.shader

import com.jevzo.flappy.utils.Matrix4f
import com.jevzo.flappy.utils.Vector3f
import org.lwjgl.opengl.GL20.*


class Shader(
    vertexShader: String, fragmentShader: String
) {

    private val shaderFactory = ShaderFactory()
    private val uniformLocationCache: MutableMap<String, Int> = mutableMapOf()

    private var id: Int = 0
    private var enabled: Boolean = false

    init {
        id = shaderFactory.load(vertexShader, fragmentShader)
    }

    fun getUniform(name: String): Int {
        if (uniformLocationCache.containsKey(name)) {
            return uniformLocationCache[name]!!
        }

        val result = glGetUniformLocation(id, name)

        if (result == -1) {
            throw RuntimeException("Could not find uniform variable '$name'")
        }

        uniformLocationCache[name] = result

        return result
    }

    fun setUniform1i(name: String?, value: Int) {
        if (!enabled) this.enable()
        glUniform1i(getUniform(name!!), value)
    }

    fun setUniform1f(name: String?, value: Float) {
        if (!enabled) this.enable()
        glUniform1f(getUniform(name!!), value)
    }

    fun setUniform2f(name: String?, x: Float, y: Float) {
        if (!enabled) this.enable()
        glUniform2f(getUniform(name!!), x, y)
    }

    fun setUniform3f(name: String?, vector: Vector3f) {
        if (!enabled) this.enable()
        glUniform3f(getUniform(name!!), vector.x, vector.y, vector.z)
    }

    fun setUniformMatrix4f(name: String?, matrix: Matrix4f) {
        if (!enabled) this.enable()
        glUniformMatrix4fv(getUniform(name!!), false, matrix.toFloatBuffer())
    }

    fun enable() {
        glUseProgram(id)
        enabled = true
    }

    fun disable() {
        glUseProgram(0)
        enabled = false
    }

    companion object {
        private var backgroundShader: Shader? = null
        private var birdShader: Shader? = null
        private var pipeShader: Shader? = null

        fun background(): Shader {
            if (backgroundShader == null) {
                backgroundShader =
                    Shader("shaders/background/background_vertex.glsl", "shaders/background/background_fragment.glsl")
            }

            return backgroundShader!!
        }

        fun bird(): Shader {
            if (birdShader == null) {
                birdShader =
                    Shader("shaders/bird/bird_vertex.glsl", "shaders/bird/bird_fragment.glsl")
            }

            return birdShader!!
        }

        fun pipe(): Shader {
            if (pipeShader == null) {
                pipeShader =
                    Shader("shaders/pipe/pipe_vertex.glsl", "shaders/pipe/pipe_fragment.glsl")
            }

            return pipeShader!!
        }
    }
}