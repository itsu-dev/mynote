package dev.itsu.mynote.ui.note

import dev.itsu.mynote.data.Settings
import dev.itsu.mynote.ui.note.pen.JavaFXPenOwner
import javafx.scene.Cursor
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Label
import javafx.scene.input.TouchEvent
import javafx.scene.paint.Color
import jpen.*
import jpen.event.PenListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.min

class WritableCanvas : ResizableCanvas() {

    private var xPoints = mutableListOf<Double>()
    private var yPoints = mutableListOf<Double>()
    private var x = 0.0
    private var y = 0.0
    private val gc = this.graphicsContext2D

    init {
        gc.stroke = Color.BLACK
        gc.lineWidth = 1.0

        cursor = if (Settings.drawWithPen) Cursor.TEXT else Cursor.DEFAULT

        val penOwner = JavaFXPenOwner(this)
        val manager = PenManager(penOwner)
        var pX = 0.0
        var pY = 0.0
        var pressure = 0.0F
        manager.pen.addListener(object : PenListener {
            override fun penLevelEvent(e: PLevelEvent) {
                if (!Settings.drawWithPen) return

                pressure = e.pen.getLevelValue(PLevel.Type.PRESSURE)
                if (pressure > 0.0) {
                    if (x == -1.0) {
                        x = e.pen.getLevelValue(PLevel.Type.X).toDouble()
                        y = e.pen.getLevelValue(PLevel.Type.Y).toDouble()
                    } else {
                        pX = e.pen.getLevelValue(PLevel.Type.X).toDouble()
                        pY = e.pen.getLevelValue(PLevel.Type.Y).toDouble()
                        gc.lineWidth = 1.0 + if (pressure > 0.4) (pressure - 0.4) * 2 else 0.0
                        gc.strokeLine(x, y, pX, pY)
                        x = pX
                        y = pY
                    }

                } else {
                    x = -1.0
                    y = -1.0
                }
            }

            override fun penKindEvent(e: PKindEvent) { }
            override fun penButtonEvent(e: PButtonEvent) { }
            override fun penScrollEvent(e: PScrollEvent) { }
            override fun penTock(e: Long) { }
        })

        this.setOnMousePressed {
            if (!Settings.drawWithPen) {
                if (it.isSynthesized && Note.instance!!.enableScrollByTouch) return@setOnMousePressed

                xPoints.add(it.x)
                yPoints.add(it.y)
                x = it.x
                y = it.y
            }
        }

        this.setOnMouseDragged {
            if (!Settings.drawWithPen) {
                if (it.isSynthesized && Note.instance!!.enableScrollByTouch) return@setOnMouseDragged

                gc.lineWidth = 2.0
                gc.globalAlpha = 0.8
                gc.strokeLine(x, y, it.x, it.y)

                gc.lineWidth = 1.0
                gc.stroke = Color.BLACK
                gc.strokeLine(x, y, it.x, it.y)

                x = it.x
                y = it.y
            }
        }

        this.setOnMouseReleased {
            if (!Settings.drawWithPen) {
                if (it.isSynthesized && Note.instance!!.enableScrollByTouch) return@setOnMouseReleased

                xPoints.clear()
                yPoints.clear()
            }
        }

    }

}
