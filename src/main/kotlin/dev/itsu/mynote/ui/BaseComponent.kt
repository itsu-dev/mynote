package dev.itsu.mynote.ui

import dev.itsu.mynote.ui.menubar.MenuBar
import dev.itsu.mynote.ui.note.Note
import dev.itsu.mynote.ui.sidebar.SideMenu
import javafx.animation.FadeTransition
import javafx.scene.layout.*
import javafx.util.Duration

class BaseComponent : AnchorPane() {

    val menuBar = MenuBar()
    val sideMenu = SideMenu()
    val noteCanvas = Note()
    private val base = VBox()

    companion object {
        val fadeTransition = FadeTransition(Duration.seconds(0.1)).also {
            it.fromValue = 0.0
            it.toValue = 1.0
        }
    }

    init {
        base.children.addAll(
            menuBar,
            BorderPane().also {
                it.left = sideMenu
                it.center = noteCanvas
                it.id = "base-border-pane"
                VBox.setVgrow(it, Priority.ALWAYS)
            }
        )
        this.children.addAll(base)

        setTopAnchor(base, 0.0)
        setBottomAnchor(base, 0.0)
        setLeftAnchor(base, 0.0)
        setRightAnchor(base, 0.0)
    }

}