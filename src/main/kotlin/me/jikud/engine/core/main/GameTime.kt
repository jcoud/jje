package me.jikud.engine.core.main

import me.jikud.engine.core.helpers.format

object GameTime {

    private val timer = Timer().apply { start() }

    enum class TimerNames {
        MLS, SEC, MIN, HRS
    }

    fun mls(t: Double = timer.current * 1.0): Int {
        return (t % 1000).toInt()
    }

    fun sec(t: Double = timer.current * 1.0): Double {
        val d = t / 1000 % 60
        return d.format(3).toDouble()
    }

    fun min(t: Double = timer.current * 1.0): Double {
        val d = t / 1000 / 60 % 60
        return d.format(3).toDouble()
    }

    fun hrs(t: Double = timer.current * 1.0): Double {
        val d = t / 1000 / 3600
        return d.format(3).toDouble()
    }

    override fun toString(): String {
        return "{now: ${timer.current} | " +
                "mls: ${mls().format(3)} | " +
                "sec: ${sec().format(3)} | " +
                "min: ${min().format(3)} | " +
                "hrs: ${hrs().format(3)}}"
    }
}

