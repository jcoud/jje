package me.jikud.engine.core.gui.event.mouse

abstract class AMouseAction : IMouseAction {
//    override fun mouseAction(e: EventMouseAction) {}

    override fun mouseClicked(e: EventMouseAction) {}

    override fun mouseMoved(e: EventMouseAction) {}

    override fun mouseReleased(e: EventMouseAction) {}
}