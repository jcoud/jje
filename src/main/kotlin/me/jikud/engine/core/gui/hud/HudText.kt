package me.jikud.engine.core.gui.hud

import me.jikud.engine.core.main.GLRenderHelper.FontRendering
import me.jikud.engine.core.helpers.CColor
import java.awt.Color
import kotlin.math.abs

open class HudText(width: Int, height: Int, var text: String) : HudComponent(width, height) {
    var textColor = CColor(Color.WHITE)

    init {
        showBackground = false
    }

    override fun draw() {
        super.draw()
        if (text.isEmpty()) return
        val b = FontRendering.bounds(text)
        FontRendering.fontSize =
            when {
                width < b.width -> (width / b.width)
                height < b.height -> (height / b.height)
                else -> 1f
            }
        val w = width - b.width * FontRendering.fontSize
        val h = height - b.height * FontRendering.fontSize
        val x: Float = (pos.x + w / 2)
        val y: Float = (pos.y + h / 2)
        FontRendering.drawString(textColor, text, x, y)
    }
}