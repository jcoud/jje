package me.jikud.engine.core.gui.hud

import me.jikud.engine.core.main.GLRenderHelper
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import java.awt.Color

abstract class HudComponent(
    var width: Int,
    var height: Int,
) {

    companion object {
        const val UI_OVERLAY_COVER: Byte = 1
        const val UI_OVERLAY_EQUAL: Byte = 0
        const val UI_OVERLAY_UNDER: Byte = -1

        const val UI_OVERLAY_LEVEL: Byte = UI_OVERLAY_COVER
    }

    enum class Alignment {
        HORIZONTAL, VERTICAL, NONE
    }

    var componentUILevel = 0
    var pos = PPoint.ZERO

    var background = CColor(Color.BLACK)
    var foreground = CColor(Color.WHITE)
    var showBackground = true
    var showForeground = true
    var show = true

    var gap = 0
    var strokeThickness = 1f

    open fun draw() = with(GLRenderHelper.JGL) {
        if (!show) return
        val c = if (showBackground) background else CColor(1f, 1f, 1f, 0f)

        glColor4f(c.r, c.g, c.b, c.a)
        GLRenderHelper.Rect(
            pos.x,
            pos.y,
            width - gap * 2f,
            height - gap * 2f,
            true
        )
//            return
        if (showForeground) {
            glColor4f(foreground.r, foreground.g, foreground.b, foreground.a)
            GLRenderHelper.Rect(
                pos.x + strokeThickness/2,
                pos.y + strokeThickness/2,
                width - gap * 2f - strokeThickness,
                height - gap * 2f - strokeThickness,
                false,
                strokeThickness
            )
        }
    }

    open fun update() {}

    open fun postUpdate() {}


    fun inBounds(pos: PPoint): Boolean {
        return inBounds(pos.x, pos.y)
    }

    fun inBounds(x: Float, y: Float): Boolean {
        return x in pos.x..pos.x + width - 1 && y in pos.y..pos.y + height - 1
    }
}