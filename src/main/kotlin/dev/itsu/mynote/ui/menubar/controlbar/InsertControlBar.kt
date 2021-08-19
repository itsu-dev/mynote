package dev.itsu.mynote.ui.menubar.controlbar

class InsertControlBar : AbstractControlBar() {

    init {
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/undo.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/redo.png")))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/table.png"), "表"))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/file.png"), "ファイル"))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/image.png"), "画像"))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/video.png"), "動画"))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/audio.png"), "オーディオ"))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/link.png"), "リンク"))

    }

}