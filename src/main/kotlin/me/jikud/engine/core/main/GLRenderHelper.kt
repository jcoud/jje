package me.jikud.engine.core.main

import com.jogamp.opengl.GL2
import com.jogamp.opengl.GL2.*
import com.jogamp.opengl.util.awt.TextRenderer
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.properties.Delegates


object GLRenderHelper {

    var JGL by Delegates.notNull<GL2>()

    object FontRendering {
        lateinit var fontRenderer: TextRenderer
        var fontSize = 1f

        fun bounds(text: String): PPoint.Dims {
            val b = fontRenderer.getBounds(text)
            return PPoint.Dims(b.width, b.height)
        }

        fun drawString(color: CColor, text: String, x: Float, y: Float) = with(JGL){
            glPushMatrix()
            glTranslatef(x, y, 0f)
            glScalef(fontSize, fontSize, 0f)

            fontRenderer.begin3DRendering()
            fontRenderer.setColor(color.color)
            fontRenderer.smoothing = true
            fontRenderer.draw3D(text, 0f, 0f, 0f, 1f)
            fontRenderer.end3DRendering()
            fontRenderer.flush()

            glPopMatrix()
        }
    }

    fun Rect(x: Float, y: Float, width: Float, height: Float, fill: Boolean, lineScale: Float = 1f) = with(JGL) {
        glPushMatrix()
        glTranslatef(x, y, 0f)
        glLineWidth(lineScale)
        if (fill) {
            glBegin(GL_QUADS)
            glVertex2f(0f, 0f)
            glVertex2f(width, 0f)
            glVertex2f(width, height)
            glVertex2f(0f, height)
        } else {
            glBegin(GL_LINE_LOOP)
            val sc = lineScale * .5f
            glVertex2f(- sc, 0f); glVertex2f(width + sc, 0f)
            glVertex2f(width, 0f); glVertex2f(width, height)
            glVertex2f(width + sc, height); glVertex2f(-sc, height)
            glVertex2f(0f, height); glVertex2f(0f, 0f)
        }
        glEnd()
        glPopMatrix()
        glLoadIdentity()

    }

    fun Circle(x: Float, y: Float, r: Float, fill: Boolean, lineScale: Float = 1f) = with(JGL) {
        glPushMatrix()
        glTranslatef(x, y, 0f)
        if (fill) glBegin(GL_POLYGON)
        else {
            glLineWidth(lineScale)
            glBegin(GL_LINE_LOOP)
        }
        for (i in 0..360 step 3) {
            glVertex2d(r * cos(Math.toRadians(i.toDouble())), r * sin(Math.toRadians(i.toDouble())))
        }
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    fun Line(x1: Float, y1: Float, x2: Float, y2: Float, lineScale: Float = 1f) = with(JGL) {
        glPushMatrix()
        glLineWidth(lineScale)
        glBegin(GL_LINES)
        glVertex2f(x1, y1)
        glVertex2f(x2, y2)
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    fun Polygon(fax: FloatArray, fay: FloatArray, n: Int, fill: Boolean, lineScale: Float = 1f) = with(JGL) {
        var ax = 0f
        var ay = 0f
        for (i in 0 until n) {
            val ii = if (i + 1 > n - 1) 0 else i + 1
            ax += fax[i] * fay[ii]
            ay += fax[ii] * fay[i]
        }
        val a = abs(ax - ay) / 2f
        var cx = 0f
        var cy = 0f
        for (i in 0 until n) {
            val ii = if (i + 1 > n - 1) 0 else i + 1
            cx += (fax[i] + fax[ii]) * (fax[i] * fay[ii] - fax[ii] * fay[i])
            cy += (fay[i] + fay[ii]) * (fax[i] * fay[ii] - fax[ii] * fay[i])
        }
        val xx = cx / (6 * a)
        val yy = cy / (6 * a)
        glPushMatrix()
        glTranslatef(xx, yy, 0f)
        if (fill) glBegin(GL_POLYGON)
        else {
            glLineWidth(lineScale)
            glBegin(GL_LINE_LOOP)
        }
        for (i in 0 until n) {
            glVertex2f(fax[i], fay[i])
        }
        glEnd()
        glPopMatrix()
//        glLoadIdentity()
    }
}