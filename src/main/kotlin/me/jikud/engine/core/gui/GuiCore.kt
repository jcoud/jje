package me.jikud.engine.core.gui

import me.jikud.engine.core.EngineStates
import me.jikud.engine.core.gui.event.key.EventKeyAction
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.engine.core.gui.event.mouse.IOMouseEventType
import me.jikud.engine.core.gui.event.IOEventProcessor
import me.jikud.engine.core.gui.event.key.IOKeyEventType
import me.jikud.engine.core.gui.hud.HudButton
import me.jikud.engine.core.gui.hud.HudPanel
import me.jikud.engine.core.gui.marks.DisabledEventHudMark
import me.jikud.engine.core.gui.marks.InGameGui
import me.jikud.engine.core.gui.marks.MainMenuGui
import java.awt.event.KeyEvent
import java.lang.Error
import java.lang.Exception


object GuiCore {
    var currentGui: GUI? = null
    var previousGui: GUI? = null
    var mainMenuGui: MainMenuGui? = null
    var inGameGui: InGameGui? = null
    var guiTargetAreaX = 0
    var guiTargetAreaY = 0

    @PublishedApi
    internal val guiObjects = HashMap<String, GUI>()


    inline fun <reified T : GUI> getGui(): T {
        return guiObjects[T::class.java.simpleName] as T
    }

    class GuiRegistration {

        inline fun <reified T : GUI> register() {
            val gui = T::class.java.getDeclaredConstructor().newInstance()
            if (gui is MainMenuGui) mainMenuGui = gui
            else if (gui is InGameGui) inGameGui = gui
            guiObjects[T::class.java.simpleName] = gui
        }
    }

    class IOEventProcessingImpl : IOEventProcessor {
        override fun processMouseEvent(e: EventMouseAction): Boolean {
            if (currentGui == null) return false
            recursiveCountProtector = 0
            return processMouseEventInternal(currentGui!!.canvas(), e)
        }

        override fun processKeyEvent(e: EventKeyAction): Boolean {
            return processKeyEventInternal(e)
        }



        private var recursiveCountProtector = 0

        private fun processMouseEventInternal(c: HudPanel, e: EventMouseAction): Boolean {
//        if (recursiveCountProtector > c.componentUILevel)
            try {
                if (recursiveCountProtector > 10) throw Error("Too much recursion while processing through huds.")
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            if (c.inBounds(e.pos)) {
                for (cc in c.components) {
                    if (cc is HudPanel) {
                        return processMouseEventInternal(cc, e)
                    }
                    if (cc !is HudButton) continue
                    val inButtonBounds = cc.inBounds(e.pos)
                    cc.eventType = if (inButtonBounds) e.type else IOMouseEventType.NONE
                    if (!cc.active) continue
                    when (cc.eventType) {
                        IOMouseEventType.CLICKED -> cc.action?.mouseClicked(e)
                        IOMouseEventType.MOVED -> cc.action?.mouseMoved(e)
                        IOMouseEventType.RELEASED -> cc.action?.mouseReleased(e)
                        IOMouseEventType.NONE -> continue
                    }
                }
                return true
            }

            for (cc in c.components) {
                if (cc is HudPanel) {
                    return processMouseEventInternal(cc, e)
                }
                if (cc !is HudButton) continue
                cc.eventType = IOMouseEventType.NONE
            }
            if (currentGui is DisabledEventHudMark) return false
            if (!(e.pos.xi in 0..guiTargetAreaX && e.pos.yi in 0..guiTargetAreaY)) return false
            //checking if event was in GUI or in the world
            return false
        }

        private fun processKeyEventInternal(e: EventKeyAction): Boolean {
            //todo: установленное меню по PRESSED сразу сбрасывается из-за RELEASED | пофиксить

            if (e.type != IOKeyEventType.PRESSED) return false
            when (e.keyCode) {
                KeyEvent.VK_ESCAPE -> {
                    performPause()
                    EngineStates.draw = !EngineStates.draw
                    switchToAnotherGuiGroup(mainMenuGui!!)
                }
                KeyEvent.VK_P -> performPause()
                KeyEvent.VK_D -> EngineStates.isDebug = EngineStates.isDebug
            }
            return false
        }

        private fun performPause() {
            EngineStates.timerIsUpdatable = !EngineStates.timerIsUpdatable
        }

    }


    fun processPostUpdate() {
        currentGui?.canvas()?.postUpdate()
    }

    fun processDraw() {
        currentGui?.canvas()?.draw()
    }

    fun processUpdate() {
        currentGui?.canvas()?.update()
    }

    fun switchToAnotherGuiGroup(gui: GUI) {
        if (currentGui == gui) return
        previousGui = currentGui
        currentGui = gui
        if (EngineStates.isDebug) println(gui::class.java.simpleName)
    }

    fun postInit() {
        if (mainMenuGui == null) {
            throw Error("MainMenu Gui is Null! Registrate MainMenu Gui firs! See: @${GuiRegister::javaClass.name}")
        }
        switchToAnotherGuiGroup(mainMenuGui!!)
//        current_gui?.post()
    }


}