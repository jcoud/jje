package me.jikud.engine.core.main

import com.jogamp.newt.event.KeyEvent
import com.jogamp.newt.event.KeyListener
import com.jogamp.newt.event.MouseEvent
import com.jogamp.newt.event.MouseListener
import com.jogamp.newt.opengl.GLWindow
import me.jikud.engine.core.EngineStates
import me.jikud.engine.core.gui.GuiCore
import me.jikud.engine.core.gui.event.IOEventProcessor
import me.jikud.engine.core.gui.event.key.EventKeyAction
import me.jikud.engine.core.gui.event.key.IOKeyEventType
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.engine.core.gui.event.mouse.IOMouseEventType
import me.jikud.engine.core.helpers.PPoint


internal class IOTransfer(c: GLWindow) : MouseListener, KeyListener {

    companion object {
        private val ioProcessorCore: IOEventProcessor = GuiCore.IOEventProcessingImpl()
        var ioProcessorOther: IOEventProcessor? = null
        internal var ioKey: IOComponent.KeyIO? = null
        internal var ioMouse: IOComponent.MouseIO? = null
    }


    init {
        c.addKeyListener(this)
        c.addMouseListener(this)
//        c.addMouseMotionListener(this)
    }

    override fun mouseReleased(e: MouseEvent) {
        val y = JOGLEntry.INIT_HEIGHT - e.y
        val ev = EventMouseAction(PPoint(e.x, y), IOMouseEventType.RELEASED)
        ioMouse?.mouseReleased(ev)
        prcsM(ev)
        IOComponent.lastMouseEventType = IOMouseEventType.RELEASED
        IOComponent.lastMouseButtonPressed = "none"
    }

    override fun mousePressed(e: MouseEvent) {
        val y = JOGLEntry.INIT_HEIGHT - e.y
        val ev = EventMouseAction(PPoint(e.x, y), IOMouseEventType.CLICKED)
        ioMouse?.mousePressed(ev)
        prcsM(ev)
        IOComponent.lastMouseEventType = IOMouseEventType.CLICKED
        IOComponent.x = e.x
        IOComponent.y = y
        IOComponent.lastMouseButtonPressed = e.button.toString()
    }

    override fun mouseMoved(e: MouseEvent) {
        val y = JOGLEntry.INIT_HEIGHT - e.y
        val ev = EventMouseAction(PPoint(e.x, y), IOMouseEventType.MOVED)
        ioMouse?.mouseMoved(ev)
        prcsM(ev)
        IOComponent.lastMouseEventType = IOMouseEventType.MOVED
        IOComponent.x = e.x
        IOComponent.y = y
        IOComponent.lastMouseButtonPressed = "none"
    }

    //    override fun mouseWheelMoved(p0: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseDragged(e: MouseEvent) {}
    override fun mouseWheelMoved(e: MouseEvent) {}
    override fun mouseClicked(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}

    override fun keyPressed(e: KeyEvent) {
        val ev = EventKeyAction(e.keyCode.toInt(), e.keyChar, IOKeyEventType.PRESSED)
        ioKey?.keyPressed(ev)
        prcsK(ev)
        IOComponent.lastKeyPressedCode = e.keyCode.toString()
        IOComponent.lastKeyPressedChar = e.keyChar.toString()
    }

    override fun keyReleased(e: KeyEvent) {
        val ev = EventKeyAction(e.keyCode.toInt(), e.keyChar, IOKeyEventType.RELEASED)
        ioKey?.keyReleased(ev)
        prcsK(ev)
        IOComponent.lastKeyPressedCode = "none"
        IOComponent.lastKeyPressedChar = "none"

    }

    private fun prcsM(ev: EventMouseAction) {
        val b: Boolean
        if (EngineStates.ioProcessingPriority == EngineStates.IOProcessPriority.GUI) {
            b = ioProcessorCore.processMouseEvent(ev)
            if (!b) ioProcessorOther?.processMouseEvent(ev)
        } else {
            b = ioProcessorOther?.processMouseEvent(ev)!!
            if (!b) ioProcessorCore.processMouseEvent(ev)
        }
    }

    private fun prcsK(ev: EventKeyAction) {
        val b: Boolean
        if (EngineStates.ioProcessingPriority == EngineStates.IOProcessPriority.GUI) {
            b = ioProcessorCore.processKeyEvent(ev)
            if (!b) ioProcessorOther?.processKeyEvent(ev)
        } else {
            b = ioProcessorOther?.processKeyEvent(ev)!!
            if (!b) ioProcessorCore.processKeyEvent(ev)
        }
    }

}