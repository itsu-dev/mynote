package dev.itsu.mynote.ui.note

import javafx.animation.ScaleTransition
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration
import java.text.SimpleDateFormat
import java.util.*

class Note : ScrollPane() {

    private var lockToScroll = false
    private var isLastScrollByTouch = false

    var enableScrollByTouch = false // 指でのスクロールを可能にするか

    private val titleInput = TextField("無題のページ")
    private val dateLabel = Label()
    private val pane = Pane()

    private val stackPane = StackPane().also {
        it.id = "note-canvas"
    }

    private val ruledCanvas = RuledLineCanvas().also {
        it.id = "note-ruled-canvas"
    }

    private val writableCanvas = WritableCanvas().also {
        it.id = "note-writable-canvas"
    }

    companion object {
        var instance: Note? = null
    }

    init {
        instance = this

        /*
        this.skinProperty().addListener { _ ->
            val skin = this.skin as ScrollPaneSkin
            val vBar = skin::class.java.getDeclaredField("vsb").also {
                it.isAccessible = true
            }.get(skin) as ScrollBar
            vBar.unitIncrement = vBar.unitIncrement * 10

            val hBar = skin::class.java.getDeclaredField("hsb").also {
                it.isAccessible = true
            }.get(skin) as ScrollBar
            hBar.unitIncrement = hBar.unitIncrement * 10
        }

         */

        titleInput.id = "note-title-input"
        dateLabel.id = "note-date-label"

        val vBox = VBox(titleInput, dateLabel).also {
            it.alignment = Pos.TOP_LEFT
            it.spacing = 4.0
            it.relocate(48.0, 48.0)
        }

        writableCanvas.width = 2000.0
        writableCanvas.height = 1000.0
        setPen(Pen(3.0, 0.4, Color.DARKGREEN))
        writableCanvas.relocate(0.0, 0.0)

        ruledCanvas.widthProperty().bind(this.widthProperty())
        ruledCanvas.heightProperty().bind(this.heightProperty())
        ruledCanvas.widthProperty().bind(writableCanvas.widthProperty())
        ruledCanvas.heightProperty().bind(writableCanvas.heightProperty())
        ruledCanvas.relocate(0.0, 0.0)

        pane.children.addAll(ruledCanvas, writableCanvas, vBox)
        stackPane.children.add(pane)

        this.id = "note-canvas-scroll-pane"
        this.content = stackPane

        this.setOnScrollStarted {
            if (!enableScrollByTouch) lockToScroll = true
        }

        this.setOnScrollFinished {
            if (!enableScrollByTouch) {
                isLastScrollByTouch = true
                lockToScroll = false
            }
        }

        this.setOnScroll {
            if (!enableScrollByTouch) {
                lockToScroll = false
                isLastScrollByTouch = false
            }
        }

        this.addEventFilter(ScrollEvent.SCROLL) {
            if (lockToScroll || (isLastScrollByTouch && it.isInertia)) it.consume()
        }

        setDate(System.currentTimeMillis())
    }

    fun getTitle() = titleInput.text

    fun setTitle(title: String) {
        titleInput.text = title
    }

    fun getDate() = dateLabel.text

    fun setDate(date: String) {
        dateLabel.text = date
    }

    fun setDate(date: Long) {
        dateLabel.text = SimpleDateFormat("yyyy年M月d日   H:mm").format(Date(date))
    }

    fun zoomIn() {
        val transition = ScaleTransition(Duration.seconds(0.1), stackPane)
        transition.byX = 0.1
        transition.byY = 0.1
        transition.byZ = 0.0
        transition.play()
    }

    fun zoomOut() {
        if (stackPane.scaleX - 0.1 < 1.0) return

        val transition = ScaleTransition(Duration.seconds(0.1), stackPane)
        transition.byX = -0.1
        transition.byY = -0.1
        transition.byZ = 0.0
        transition.play()
    }

    fun setCanvasCursor(cursor: Cursor) {
        writableCanvas.cursor = cursor
    }

    fun setPen(pen: Pen) {
        writableCanvas.pen = pen
    }

}