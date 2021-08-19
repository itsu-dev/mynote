package dev.itsu.mynote.ui.menubar.controlbar

import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.HBox

abstract class AbstractControlBar : HBox() {

    init {
        this.id = "control-bar-box"
        this.alignment = Pos.CENTER_LEFT
    }

    fun addControlItem(node: Node) {
        this.children.add(node)
    }

}