package com.jevzo.flappy.keyboard

import org.lwjgl.glfw.GLFWKeyCallback

class KeyboardCallback : GLFWKeyCallback() {

    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        keys[key] = action != 0
    }

    companion object {
        val keys = BooleanArray(65536)

        fun isKeyDown(key: Int): Boolean {
            return keys[key]
        }
    }
}