package dev.itsu.mynote.ui.menubar.controlbar

import dev.itsu.mynote.data.Settings
import javafx.scene.layout.Region

class DrawControlBar : AbstractControlBar() {

    init {
        addControlItem(ControlBarItem(DrawControlBar::class.java.classLoader.getResourceAsStream("icon/undo.png")))
        addControlItem(ControlBarItem(DrawControlBar::class.java.classLoader.getResourceAsStream("icon/redo.png")))
        addControlItem(Region().also { it.setPrefSize(16.0, 1.0) })
        addControlItem(ControlBarItem(DrawControlBar::class.java.classLoader.getResourceAsStream("icon/finger.png")).also {
            it.toggle = true
            it.setSelected(!Settings.drawWithPen)
            it.setOnClick {
                Settings.drawWithPen = !it.isItemSelected
            }
        })
    }

}