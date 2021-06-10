package me.jikud.engine.core.main

import me.jikud.engine.core.helpers.format
import kotlin.system.exitProcess

object Loop {
    private var fps = 0
    private var ups = 0
    private var running = false
    var dt = 0L
    var unprocessed = 0.0
    private val nsPerTick = 1_000_000_000L / JOGLEntry.FPS

    fun loop(
        init: Runnable,
        update: Runnable,
    ) {
        init.run()
        var lastTime = System.nanoTime()
        var shouldRender: Boolean
        fps = 0
        ups = 0
        var lt = System.currentTimeMillis()
        while (running) {
            shouldRender = false
            val currentTime = System.nanoTime()
            unprocessed += (currentTime - lastTime) / nsPerTick
            dt = (currentTime - lastTime) / nsPerTick
            lastTime = currentTime
            while (unprocessed >= 1) {
                ups++
                unprocessed--
                shouldRender = true
            }
            update.run()
            if (shouldRender) {
                fps++
                JOGLEntry.renderCanvasUpdate()
            }

            try {
                Thread.sleep((((1 - unprocessed) * 1000).toInt() / JOGLEntry.FPS).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            if (System.currentTimeMillis() - lt > 1000) {
                lt = System.currentTimeMillis()
//                print("ups: $ups, fps: $fps\r")
                fps = 0
                ups = 0
            }
        }
        exitProcess(0)

    }

    fun start() {
        running = true
    }

    fun stop() {
        running = false
    }

    override fun toString(): String {
        return "{ups: ${ups.format(2)} | " +
                "fps: ${fps.format(2)} | " +
                "dt: $dt | " +
                "unp: $unprocessed | " +
                "nsp: $nsPerTick}"
    }
}