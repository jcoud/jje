package me.jikud.engine.core

import me.jikud.engine.core.gui.GuiCore
import me.jikud.engine.core.gui.GuiRegister
import me.jikud.engine.core.main.IOComponent
import me.jikud.engine.core.main.JOGLEntry
import me.jikud.engine.core.main.Loop
import me.jikud.engine.core.main.IOTransfer
import me.jikud.engine.core.gui.event.IOEventProcessor

abstract class Engine : Runnable {

    var settings: EngineSettings? = null
        private set


    fun setSettings(settings: EngineSettings) {
        this.settings = settings
        JOGLEntry.FPS = settings.fps
        JOGLEntry.FONT = settings.textFont
        JOGLEntry.INIT_HEIGHT = settings.windowHeight
        JOGLEntry.INIT_WIDTH = settings.windowWidth
        JOGLEntry.TITLE = settings.title
    }

    fun loadStartMenu() {
        GuiCore.postInit()
    }

    abstract fun init()
    abstract fun update()
    abstract fun render()

    private fun initProc() {
        init()
        JOGLEntry.init()
    }

    fun setEventProcessor(ioEventProcessor: IOEventProcessor) {
        IOTransfer.ioProcessorOther = ioEventProcessor
    }

    fun setIOKeyComponent(io: IOComponent.KeyIO) {
        IOTransfer.ioKey = io
    }

    fun setIOMouseComponent(io: IOComponent.MouseIO) {
        IOTransfer.ioMouse = io
    }

    fun start() {
        Thread(this).start()
        Loop.start()
    }

    fun stop() {
        Loop.stop()
    }

    fun setGuiRegister(guiRegister: GuiRegister) {
        guiRegister.init()
    }

    private fun updateProc() {
        update()
        GuiCore.processUpdate()
    }


    private fun renderProc() {
        render()
    }

    override fun run() {
        JOGLEntry.render(this::renderProc)
        Loop.loop(this::initProc, this::updateProc)
    }
}