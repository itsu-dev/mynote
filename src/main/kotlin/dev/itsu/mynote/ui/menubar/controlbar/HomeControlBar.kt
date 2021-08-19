package dev.itsu.mynote.ui.menubar.controlbar

import javafx.scene.layout.Region

class HomeControlBar : AbstractControlBar() {

    init {
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/undo.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/redo.png")))
        addControlItem(Region().also { it.setPrefSize(16.0, 1.0) })
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/select.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/spacing.png")))
        addControlItem(Region().also { it.setPrefSize(16.0, 1.0) })
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/bold.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/italic.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/underline.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/font_color.png")))
        addControlItem(Region().also { it.setPrefSize(16.0, 1.0) })
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/list.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/numbered_list.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/markdown.png")))
    }

}