package dev.itsu.mynote.data

import dev.itsu.mynote.ui.note.Note
import javafx.scene.Cursor

object Settings {

    const val NOTE_THEME_DARK = "dark"
    const val NOTE_THEME_LIGHT = "light"

    var doAnimation = true
    var colorTheme = "dark"
    var noteTheme = NOTE_THEME_LIGHT
    var drawWithPen = true
        set(b) {
            field = b
            Note.instance?.setCanvasCursor(if (b) Cursor.TEXT else Cursor.DEFAULT)
        }

}