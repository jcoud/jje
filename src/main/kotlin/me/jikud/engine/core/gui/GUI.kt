package me.jikud.engine.core.gui

import me.jikud.engine.core.main.GLRenderHelper
import me.jikud.engine.core.gui.hud.HudPanel

abstract class GUI {
    //    private var canvas: HudPanel? = null

    abstract val name: String

    abstract fun canvas(): HudPanel


}

