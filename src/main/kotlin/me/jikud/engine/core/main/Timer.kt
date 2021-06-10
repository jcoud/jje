package me.jikud.engine.core.main

import me.jikud.engine.core.EngineStates

class Timer {
    companion object {
        private var deltaPaused = 0L
        private var lastPaused = 0L
        private var deltaLast = 0L
        private var paused = false

        val timers = ArrayList<Timer>()
        private var timerClassCounter = 0

        fun update() {
            //works only once when triggered
            if (EngineStates.timerIsUpdatable) unpause()
            else pause()
            if (paused) deltaPaused = System.currentTimeMillis() - lastPaused + deltaLast
            timers.forEach(Timer::update)
        }

        private fun pause() {
            if (paused) return
            paused = true
            lastPaused = System.currentTimeMillis()
            deltaLast = deltaPaused
        }

        private fun unpause() {
            if (!paused) return
            paused = false
        }

        override fun toString(): String {
            return "{lp: $lastPaused | " +
                    "pa: $paused | " +
                    "dp: $deltaPaused}"
        }
    }

    var current = 0L
    private var started = false
    private var lastTriggered = 0L
    private var uid = 0

    init {
        this.uid = timerClassCounter
        timerClassCounter++
        timers.add(this)
    }

    private fun update() {
        if (!this.started) return
        if (!paused) this.current = System.currentTimeMillis() - this.lastTriggered - deltaPaused

    }

    fun start() {
        if (this.started) return
        this.started = true
        this.lastTriggered = System.currentTimeMillis() - deltaPaused
    }

    fun trigger(time: Double, timerName: GameTime.TimerNames = GameTime.TimerNames.SEC): Boolean {
        if (this.current == 0L) return false
        val t: Double = when (timerName) {
            GameTime.TimerNames.MLS -> GameTime.mls(this.current * 1.0) * 1.0
            GameTime.TimerNames.SEC -> GameTime.sec(this.current * 1.0)
            GameTime.TimerNames.MIN -> GameTime.min(this.current * 1.0)
            GameTime.TimerNames.HRS -> GameTime.hrs(this.current * 1.0)
        }
        if (t >= time) {
            this.lastTriggered = System.currentTimeMillis() - deltaPaused
            return true
        }
        return false
    }
}
