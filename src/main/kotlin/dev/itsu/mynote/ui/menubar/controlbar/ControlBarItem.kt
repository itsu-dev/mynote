package dev.itsu.mynote.ui.menubar.controlbar

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.io.InputStream

class ControlBarItem(icon: InputStream, text: String = "") : Label(text, ImageView(Image(icon))) {

    var toggle = false
    var isItemSelected = false

    private var onClick: () -> Unit = {}
    private var controlGroup: ControlGroup? = null

    init {
        this.id = "control-bar-item"
        this.graphicTextGap = 8.0

        this.setOnMousePressed {
            setSelected(!isItemSelected)
            controlGroup?.onControlItemClicked(this)
            onClick.invoke()
        }

        this.setOnMouseReleased {
            if (!toggle) setSelected(!isItemSelected)
        }
    }

    fun setSelected(selected: Boolean) {
        isItemSelected = selected

        if (selected) {
            this.styleClass.add("selected")
        } else {
            this.styleClass.remove("selected")
        }
    }

    fun setControlGroup(controlGroup: ControlGroup) {
        this.controlGroup = controlGroup
    }

    fun setOnClick(onClick: () -> Unit) {
        this.onClick = onClick
    }

}