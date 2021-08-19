package dev.itsu.mynote.ui.note

import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.TouchEvent
import javafx.scene.paint.Color
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
        gc.lineWidth = 2.0

        this.setOnMousePressed {
            if (it.isSynthesized && Note.instance!!.enableScrollByTouch) return@setOnMousePressed

            xPoints.add(it.x)
            yPoints.add(it.y)
            x = it.x
            y = it.y
        }

        this.setOnMouseDragged {
            if (it.isSynthesized && Note.instance!!.enableScrollByTouch) return@setOnMouseDragged

            /*
            xPoints.add(it.x)
            yPoints.add(it.y)

             */

            gc.lineWidth = 2.0
            gc.globalAlpha = 0.8
            gc.strokeLine(x, y, it.x, it.y)

            gc.lineWidth = 1.0
            gc.stroke = Color.BLACK
            gc.strokeLine(x, y, it.x, it.y)

            x = it.x
            y = it.y
        }

        this.setOnMouseReleased {
            if (it.isSynthesized && Note.instance!!.enableScrollByTouch) return@setOnMouseReleased

            //calculate(SplineInterpolator(xPoints.toDoubleArray(), yPoints.toDoubleArray()))
            xPoints.clear()
            yPoints.clear()
        }

        this.addEventHandler(TouchEvent.ANY) {
            it.consume()
        }

        /*
        GlobalScope.launch {
            var x1: Double? = null
            var x2: Double? = null
            var x3: Double? = null
            var y1: Double? = null
            var y2: Double? = null
            var y3: Double? = null

            while (true) {
                if (x1 == null) x1 = xPoints.poll()
                if (x2 == null) x2 = xPoints.poll()
                if (x3 == null) x3 = xPoints.poll()
                if (y1 == null) y1 = yPoints.poll()
                if (y2 == null) y2 = yPoints.poll()
                if (y3 == null) y3 = yPoints.poll()

                if (x1 != null && x2 != null && x3 != null
                    && y1 != null && y2 != null && y3 != null) {
                    calculate(
                        SplineInterpolator(
                            doubleArrayOf(x1, x2, x3),
                            doubleArrayOf(y1, y2, y3)
                        )
                    )
                }
            }
        }

         */

        /*
        GlobalScope.launch {
            while (true) {
                var interpolator = queue.poll()
                if (interpolator != null) {
                    calculate(interpolator)
                }
            }
        }
        */
    }

    private fun calculate(interpolator: SplineInterpolator) {
        val startX = xPoints[0]

        val min = min(startX, xPoints[xPoints.size - 1])
        val max = kotlin.math.max(startX, xPoints[xPoints.size - 1])
        var delta = 0.05
        var sx = min
        var sy = yPoints[0]
        while (min + delta <= max) {
            val x = min + delta
            val y = interpolator[x]
            gc.strokeLine(sx, sy, x, y)
            sx = x
            sy = y
            delta += 0.1
        }
    }

}
