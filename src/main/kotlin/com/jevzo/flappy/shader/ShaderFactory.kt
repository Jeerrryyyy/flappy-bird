package com.jevzo.flappy.shader

import com.jevzo.flappy.utils.FileUtils
import org.lwjgl.opengl.GL20.*
import java.io.File

class ShaderFactory {

    fun load(vertexShaderPath: String, fragmentShaderPath: String): Int {
        val vertexShader = FileUtils.readFileContents(File(vertexShaderPath))
        val fragmentShader = FileUtils.readFileContents(File(fragmentShaderPath))

        return this.createShader(vertexShader, fragmentShader)
    }

    private fun createShader(vertex: String, fragment: String): Int {
        val program = glCreateProgram()

        val vertexId = glCreateShader(GL_VERTEX_SHADER)
        val fragmentId = glCreateShader(GL_FRAGMENT_SHADER)

        glShaderSource(vertexId, vertex)
        glShaderSource(fragmentId, fragment)

        glCompileShader(vertexId)
        if (glGetShaderi(vertexId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw RuntimeException(glGetShaderInfoLog(vertexId, 2048))
        }

        glCompileShader(fragmentId)
        if (glGetShaderi(fragmentId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw RuntimeException(glGetShaderInfoLog(fragmentId, 2048))
        }

        glAttachShader(program, vertexId)
        glAttachShader(program, fragmentId)

        glLinkProgram(program)
        glValidateProgram(program)

        glDeleteShader(vertexId)
        glDeleteShader(fragmentId)

        return program
    }
}