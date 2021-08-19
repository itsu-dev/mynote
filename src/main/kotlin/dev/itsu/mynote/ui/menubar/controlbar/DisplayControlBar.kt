package dev.itsu.mynote.ui.menubar.controlbar

import dev.itsu.mynote.data.Settings
import dev.itsu.mynote.ui.UIManager
import dev.itsu.mynote.ui.note.Note

class DisplayControlBar : AbstractControlBar() {

    init {
        addControlItem(ControlBarItem(DisplayControlBar::class.java.classLoader.getResourceAsStream("icon/zoom_in.png")).also {
            it.setOnClick {
                Note.instance!!.zoomIn()
            }
        })

        addControlItem(ControlBarItem(DisplayControlBar::class.java.classLoader.getResourceAsStream("icon/zoom_out.png")).also {
            it.setOnClick {
                Note.instance!!.zoomOut()
            }
        })

        addControlItem(ControlBarItem(DisplayControlBar::class.java.classLoader.getResourceAsStream("icon/night_mode.png"), "背景色の切り替え").also {
            it.setOnClick {
                val newTheme = if (Settings.noteTheme == Settings.NOTE_THEME_DARK) Settings.NOTE_THEME_LIGHT else Settings.NOTE_THEME_DARK
                UIManager.unLoadNoteCSS()
                Settings.noteTheme = newTheme
                UIManager.loadNoteCSS()
            }
        })
    }

}