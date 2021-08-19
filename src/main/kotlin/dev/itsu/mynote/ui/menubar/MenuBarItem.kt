package dev.itsu.mynote.ui.menubar

import dev.itsu.mynote.ui.menubar.MenuBar
import javafx.scene.control.Label

class MenuBarItem(text: String, val label: String) : Label(text) {

    var isItemSelected = false
    var isSelectionCancellable = false

    private var onClick: () -> Unit = {}
    private lateinit var menuBar: MenuBar

    init {
        this.id = "menu-bar-item"

        this.setOnMouseClicked {
            if (!isSelectionCancellable && isItemSelected) return@setOnMouseClicked

            setSelected(!isItemSelected)
            menuBar.onMenuItemSelected(this)
        }
    }

    fun setSelected(selected: Boolean) {
        isItemSelected = selected

        if (selected) {
            this.styleClass.add("selected")
        } else {
            this.styleClass.remove("selected")
        }

        onClick.invoke()
    }

    fun setMenuBar(menuBar: MenuBar) {
        this.menuBar = menuBar
    }

    fun setOnClick(onClick: () -> Unit) {
        this.onClick = onClick
    }
}