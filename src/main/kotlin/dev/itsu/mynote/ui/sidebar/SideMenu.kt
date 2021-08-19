package dev.itsu.mynote.ui.sidebar

import dev.itsu.mynote.data.Settings
import dev.itsu.mynote.ui.BaseComponent
import javafx.animation.*
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.util.Duration
import kotlin.math.PI
import kotlin.math.cos

class SideMenu : HBox() {

    private val sideBarBox = VBox().also {
        it.id = "side-bar-box"
        it.alignment = Pos.TOP_CENTER
        setHgrow(it, Priority.ALWAYS)
    }

    private val leftSideMenuBox = VBox().also {
        it.id = "left-side-menu-box"
    }

    private val rightSideMenuBox = VBox().also {
        it.id = "right-side-menu-box"
    }

    private val pane = BorderPane().also {
        it.id = "side-bar-base"
    }

    init {
        this.children.addAll(sideBarBox, pane)

        addSideBarItem(SideBarItem(SideMenu::class.java.classLoader.getResourceAsStream("icon/notes.png")!!, "notes").also {
            it.setOnClick {
                if (it.isItemSelected) {
                    openSideContent("notes")
                } else {
                    closeSideContent("notes")
                }
            }
        })
        addSideBarItem(SideBarItem(SideMenu::class.java.classLoader.getResourceAsStream("icon/search.png")!!, "search"))
    }

    private fun openSideContent(label: String) {
        fun onSideContentOpened(label: String) {
            when (label) {
                "notes" -> {
                    if (Settings.doAnimation) {
                        BaseComponent.fadeTransition.node = leftSideMenuBox
                        BaseComponent.fadeTransition.play()
                    }
                    pane.left = leftSideMenuBox

                    if (Settings.doAnimation) {
                        BaseComponent.fadeTransition.node = rightSideMenuBox
                        BaseComponent.fadeTransition.play()
                    }
                    pane.right = rightSideMenuBox
                }
            }
        }

        if (Settings.doAnimation) {
            Timeline(
                KeyFrame(
                    Duration.seconds(0.0),
                    KeyValue(pane.prefWidthProperty(), 0, Interpolator.EASE_BOTH)
                ),
                KeyFrame(
                    Duration.seconds(0.5),
                    KeyValue(pane.prefWidthProperty(), 382, Interpolator.EASE_BOTH),
                ),
                KeyFrame(Duration.seconds(0.5), {
                    onSideContentOpened(label)
                })
            ).play()

        } else {
            pane.prefWidth = 382.0
            onSideContentOpened(label)
        }
    }

    private fun closeSideContent(label: String) {
        when (label) {
            "notes" -> {
                pane.left = null
                pane.right = null
            }
        }

        if (Settings.doAnimation) {
            Timeline(
                KeyFrame(
                    Duration.seconds(0.0),
                    KeyValue(pane.prefWidthProperty(), 382, Interpolator.EASE_BOTH)
                ),
                KeyFrame(
                    Duration.seconds(0.5),
                    KeyValue(pane.prefWidthProperty(), 0, Interpolator.EASE_BOTH)
                )
            ).play()

        } else {
            pane.prefWidth = 0.0
        }
    }

    fun addSideBarItem(item: SideBarItem) {
        item.setSideMenu(this)
        sideBarBox.children.add(item)
    }

    fun onSideBarItemSelected(item: SideBarItem) {
        sideBarBox.children.forEach {
            if (it is SideBarItem && it != item) {
                it.setSelected(false)
            }
        }

        when (item.label) {
            // TODO
        }
    }
}