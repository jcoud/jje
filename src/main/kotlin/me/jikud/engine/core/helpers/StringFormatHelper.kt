package me.jikud.engine.core.helpers

import java.util.*

fun Number.format(digits: Int) = "%0${digits}${if (this is Int) "d" else ".0${digits}f"}".format(Locale.CANADA, this)
