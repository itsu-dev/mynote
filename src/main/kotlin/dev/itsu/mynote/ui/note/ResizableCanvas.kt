package dev.itsu.mynote.ui.note

import javafx.scene.canvas.Canvas
import javafx.scene.control.ScrollPane
import javafx.scene.paint.Color


open class ResizableCanvas : Canvas() {

    init {
        widthProperty().addListener { _ -> repaint() }
        heightProperty().addListener { _ -> repaint() }
    }

    override fun isResizable(): Boolean {
        return true
    }

    override fun prefWidth(height: Double): Double {
        return width
    }

    override fun prefHeight(width: Double): Double {
        return height
    }

    open fun repaint() {

    }

}