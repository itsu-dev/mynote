package dev.itsu.mynote.ui.menubar.controlbar

class HelpControlBar : AbstractControlBar() {

    init {
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/help.png"), "ヘルプ"))
        addControlItem(ControlBarItem(HomeControlBar::class.java.classLoader.getResourceAsStream("icon/settings.png"), "設定"))
    }

}