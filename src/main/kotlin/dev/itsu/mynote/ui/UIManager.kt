package dev.itsu.mynote.ui

import dev.itsu.mynote.Main
import dev.itsu.mynote.data.Settings
import javafx.scene.Scene

object UIManager {

    private lateinit var baseComponent: BaseComponent
    private lateinit var scene: Scene

    fun createScene(): Scene {
        baseComponent = BaseComponent()
        scene = Scene(baseComponent, 800.0, 600.0)
        return scene
    }

    fun loadCSS() {
        scene.stylesheets.add(Main::class.java.classLoader.getResource("css/core.css")!!.toURI().toString())
        scene.stylesheets.add(Main::class.java.classLoader.getResource("css/${Settings.colorTheme}/core.css")!!.toURI().toString())
    }

    fun loadNoteCSS() {
        scene.stylesheets.add(Main::class.java.classLoader.getResource("css/${Settings.colorTheme}/note_${Settings.noteTheme}.css")!!.toURI().toString())
    }

    fun unLoadNoteCSS() {
        scene.stylesheets.remove(Main::class.java.classLoader.getResource("css/${Settings.colorTheme}/note_${Settings.noteTheme}.css")!!.toURI().toString())
    }

}