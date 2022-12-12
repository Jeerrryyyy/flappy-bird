package com.jevzo.flappy.bootstrap

import com.jevzo.flappy.FlappyBird

fun main(args: Array<String>) {
    Thread.currentThread().name = "main"

    val flappyBird = FlappyBird()
    flappyBird.start()

    Runtime.getRuntime().addShutdownHook(Thread {
        flappyBird.stop()
    })
}