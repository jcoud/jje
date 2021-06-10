package me.jikud.engine.core.helpers

import com.jogamp.opengl.math.FloatUtil.abs
import java.awt.Point
import kotlin.math.sqrt

@Suppress("SpellCheckingInspection")
public class PPoint(var x: Float, var y: Float) {
    constructor(xi: Int, yi: Int) : this(xi.toFloat(), yi.toFloat())
    constructor(xd: Double, yd: Double) : this(xd.toFloat(), yd.toFloat())
    constructor(pPoint: PPoint) : this(pPoint.x, pPoint.y)
    constructor(point: Point) : this(point.getX().toFloat(), point.getY().toFloat())

    class Dims(var width: Float, var height: Float) {
        constructor(widthI: Int, heightI: Int) : this(widthI.toFloat(), heightI.toFloat())
        constructor(widthD: Double, heightD: Double) : this(widthD.toFloat(), heightD.toFloat())
    }

    public companion object {
        val ZERO = PPoint(.0f, .0f)
        val NONE = PPoint(-1f, -1f)

        fun Add(posA: PPoint, posB: PPoint): PPoint {
            return PPoint(posA.translate(posB))
        }

        fun Add(posA: PPoint, value: Int): PPoint {
            return PPoint(posA.translate(value * 1f))
        }

        fun Sub(posA: PPoint, posB: PPoint): PPoint {
            return PPoint(posA.sub(posB))
        }

        fun SubAbs(posA: PPoint, posB: PPoint): PPoint {
            return PPoint(abs(posA.x - posB.x), abs(posA.y - posB.y))
        }

        fun snapToGrid(pos: PPoint, value: Float): PPoint {
            val xi = (pos.x / value).toInt()
            val yi = (pos.y / value).toInt()
            val xd = xi * value
            val yd = yi * value
            return PPoint(xd, yd)
        }
    }

    val xi get() = this.x.toInt()
    val yi get() = this.y.toInt()


    fun translate(x: Float, y: Float): PPoint {
        this.x += x
        this.y += y
        return this
    }

    fun translate(pos: PPoint): PPoint {
        return this.translate(pos.x, pos.y)
    }

    fun translate(value: Float): PPoint {
        return this.translate(value, value)
    }

    fun sub(x: Float, y: Float): PPoint {
        this.x -= x
        this.y -= y
        return this
    }
    fun sub(pos: PPoint): PPoint {
        this.x -= pos.x
        this.y -= pos.y
        return this
    }

    fun setLocation(x: Float, y: Float): PPoint {
        this.x = x
        this.y = y
        return this
    }

    fun setLocation(pos: PPoint): PPoint {
        return this.setLocation(pos.x, pos.y)
    }

    fun distanceSq(pos: PPoint): Float {
        return sqrt(dist(pos))
    }


    fun dist(pos: PPoint): Float {
        val xx = this.x - pos.x
        val yy = this.y - pos.y
        return xx * xx + yy * yy
    }

    fun scale(value: Int): PPoint {
        return PPoint(this.x * value, this.y * value)
    }

    fun devide(value: Int): PPoint {
        return PPoint(this.x / value, this.y / value)
    }

//    fun descendInt(value: Int): PPoint {
//        return PPoint(this.xi.div(value), this.yi.div(value))
//    }


    fun toIndex(devider: Int, multiplier: Int): Int {
        val d = devide(devider)
        return d.xi * multiplier + d.yi
    }

    override fun toString(): String {
        return "{x: $x, y: $y}"
    }

    override fun equals(other: Any?): Boolean {
        return (other is PPoint && other.x == this.x && other.y == this.y) ||
                (other is PPoint && other.xi == this.xi && other.yi == this.yi)
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }


}