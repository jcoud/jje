package me.jikud.engine.core.gui

interface GuiRegister {
    fun registrateGui(registrator: GuiCore.GuiRegistration)
    fun init() {
        registrateGui(GuiCore.GuiRegistration())
    }
}