package me.jikud.engine.core.gui.event.mouse

interface IMouseAction {
//    fun mouseAction(e: EventMouseAction)
    fun mouseClicked(e: EventMouseAction)
    fun mouseMoved(e: EventMouseAction)
    fun mouseReleased(e: EventMouseAction)
}