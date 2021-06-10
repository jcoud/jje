package me.jikud.engine.core

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import me.jikud.engine.core.helpers.FFont
import java.awt.Font
import java.io.File
import java.lang.Exception

class EngineSettings(@Transient private var settingsFile: String) {

    var fps = 60
    var windowWidth = 600
    var windowHeight = 400
    var title = "JoglEngine by JCOUD"
    var textFont: FFont = FFont("Consolas", Font.BOLD, 12)
//    val defaultSettingsName = "settings"

    init {
        premakeSettingsFileIfDoesNotExists()
        setup()
    }

    private fun premakeSettingsFileIfDoesNotExists() {
        checkForExtension()
        val f = File(settingsFile)
//        if (f.exists() && f.length() != 0L) return
/*

        val hm = hashMapOf<String, Any>(
            Pair("fps", fps),
            Pair("windowWidth", windowWidth),
            Pair("windowHeight", windowHeight),
            Pair("title", title),
            Pair("fontSettings", hashMapOf<String, Any>(
                    Pair("name", textFont.name),
                    Pair("size", textFont.size),
                    Pair("style", textFont.style),
                )
            )
        )
*/

        val tw = TomlWriter
            .Builder()
            .indentValuesBy(2)
            .indentTablesBy(2)
            .padArrayDelimitersBy(2)
            .build()
        val toml = tw.write(this)
//        val toml = tw.write(hm)

        f.createNewFile()
        f.writeText(toml)
    }

    private fun checkForExtension() {
        settingsFile = if (settingsFile.contains(".toml")) settingsFile else "$settingsFile.toml"
    }

    private fun setup() {
        if (settingsFile.isEmpty()) return
        val file = File(settingsFile)
        try {
            if (!file.exists()) {
                throw Error("Settings file \"$settingsFile\" does not exists. Applying default parameters.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
//        val toml = Toml().read(file)
        val toml = Toml().read(file).to(this.javaClass)
        fps = toml.fps
        windowWidth = toml.windowWidth
        windowHeight = toml.windowHeight
        title = toml.title
        textFont = toml.textFont
//        fps = toml.getLong("fps").toInt()
//        windowWidth = toml.getLong("windowWidth").toInt()
//        windowHeight = toml.getLong("windowHeight").toInt()
//        title = toml.getString("title")
//        val t = toml.getTable("fontSettings")
//        val fontName = t.getString("name")
//        val fontSize = t.getLong("size").toInt()
//        val fontStyle = t.getLong("style").toInt()
//        textFont = FFont(fontName, fontStyle, fontSize)
    }
}