package dev.itsu.mynote.ui.sidebar

import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import java.io.InputStream

class SideBarItem(inputStream: InputStream, val label: String) : VBox() {

    var isItemSelected = false
    var isSelectionCancellable = true

    private var onClick: () -> Unit = {}
    private lateinit var sideMenu: SideMenu

    init {
        this.id = "side-bar-item"
        this.alignment = Pos.CENTER

        val view = ImageView(Image(inputStream, 36.0, 36.0, true, true))
        view.fitWidth = 32.0
        view.fitHeight = 32.0
        this.children.add(view)

        this.setOnMouseClicked {
            if (!isSelectionCancellable && isItemSelected) return@setOnMouseClicked

            setSelected(!isItemSelected)
            sideMenu.onSideBarItemSelected(this)
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

    fun setSideMenu(sideMenu: SideMenu) {
        this.sideMenu = sideMenu
    }

    fun setOnClick(onClick: () -> Unit) {
        this.onClick = onClick
    }

}