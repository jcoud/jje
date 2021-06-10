package me.jikud.engine.core.gui.event

import me.jikud.engine.core.gui.event.key.EventKeyAction
import me.jikud.engine.core.gui.event.mouse.EventMouseAction

interface IOEventProcessor {
    fun processMouseEvent(e: EventMouseAction): Boolean
    fun processKeyEvent(e: EventKeyAction) : Boolean
}