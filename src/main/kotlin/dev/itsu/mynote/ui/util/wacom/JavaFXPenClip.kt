package dev.itsu.mynote.ui.util.wacom

import jpen.owner.PenClip
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.round

class JavaFXPenClip(private val javaFXPenOwner: JavaFXPenOwner) : PenClip {

    override fun evalLocationOnScreen(pointOnScreen: Point) {
        val converted = javaFXPenOwner.getActiveCanvas().localToScreen(0.0, 0.0)
        pointOnScreen.x = round(converted.x).toInt()
        pointOnScreen.y = round(converted.y).toInt()
    }

    override fun contains(point: Point2D.Float): Boolean {
        val activeCanvas = javaFXPenOwner.getActiveCanvas()
        return !(point.x < 0.0F)
                && !(point.y < 0.0F)
                && !(point.x > activeCanvas.width.toFloat())
                && !(point.y > activeCanvas.height.toFloat())
    }

}