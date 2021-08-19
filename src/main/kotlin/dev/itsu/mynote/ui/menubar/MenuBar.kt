package dev.itsu.mynote.ui.menubar

import dev.itsu.mynote.data.Settings
import dev.itsu.mynote.ui.BaseComponent
import dev.itsu.mynote.ui.menubar.controlbar.*
import javafx.animation.FadeTransition
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Duration

class MenuBar : VBox() {

    private val menuBarBox = HBox().also {
        it.id = "menu-bar-box"
        it.alignment = Pos.CENTER_LEFT
    }

    private var homeControlBar = HomeControlBar()
    private var insertControlBar = InsertControlBar()
    private var displayControlBar = DisplayControlBar()
    private var helpControlBar = HelpControlBar()

    init {
        this.id = "menu-bar-base"
        this.children.addAll(menuBarBox, homeControlBar)

        addItem(MenuBarItem("ファイル", "file"))
        addItem(MenuBarItem("ホーム", "home").also {
            it.setSelected(true)
        })
        addItem(MenuBarItem("挿入", "insert"))
        addItem(MenuBarItem("描画", "draw"))
        addItem(MenuBarItem("表示", "display"))
        addItem(MenuBarItem("ヘルプ", "help"))
    }

    fun addItem(item: MenuBarItem) {
        item.setMenuBar(this)
        menuBarBox.children.add(item)
    }

    fun onMenuItemSelected(item: MenuBarItem) {
        menuBarBox.children.forEach {
            if (it is MenuBarItem && it != item) it.setSelected(false)
        }

        val newControlBar =
            when (item.label) {
                "home" -> homeControlBar
                "insert" -> insertControlBar
                "display" -> displayControlBar
                "help" -> helpControlBar
                else -> null
            }

        if (newControlBar != null) {
            this.children.remove(this.children[this.children.size - 1])

            if (Settings.doAnimation) {
                BaseComponent.fadeTransition.node = newControlBar
                BaseComponent.fadeTransition.play()
            }

            this.children.add(newControlBar)
        }

    }
}