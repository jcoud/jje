package me.jikud.engine.core.main

import me.jikud.engine.core.gui.event.key.EventKeyAction
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.engine.core.gui.event.mouse.IOMouseEventType

class IOComponent {
    companion object {
        var x = 0
        var y = 0
        var lastMouseButtonPressed = ""
        var lastMouseEventType = IOMouseEventType.NONE
        var lastKeyPressedCode = ""
        var lastKeyPressedChar = ""

        //@formatter:off
        fun fieldsToString(): String =
            "x:$x | " +
            "y:$y | " +
            "lastMouseButtonPressed:$lastMouseButtonPressed | " +
            "lastMouseEventType:$lastMouseEventType"
        //@formatter:on
    }

    abstract class KeyIO {
        open fun keyPressed(e: EventKeyAction) {}
        open fun keyReleased(e: EventKeyAction) {}
    }

    abstract class MouseIO {
        open fun mouseMoved(e: EventMouseAction) {}
        open fun mousePressed(e: EventMouseAction) {}
        open fun mouseReleased(e: EventMouseAction) {}
    }
}