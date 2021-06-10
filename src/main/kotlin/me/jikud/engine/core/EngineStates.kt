package me.jikud.engine.core

object EngineStates {
    var ioProcessingPriority = IOProcessPriority.GUI
    var draw = true
    var timerIsUpdatable = false
    var isDebug = false

    enum class IOProcessPriority {
        GUI, OTHER
    }
}