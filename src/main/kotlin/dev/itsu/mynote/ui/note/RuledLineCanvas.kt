package dev.itsu.mynote.ui.note

import dev.itsu.mynote.data.Settings
import javafx.scene.paint.Color

class RuledLineCanvas : ResizableCanvas() {

    var drawRuledLine = true
    var gap: Double = 48.0
    var lattice = true

    override fun repaint() {
        if (drawRuledLine) drawRuledLine()
    }

    private fun drawRuledLine() {
        val gc = this.graphicsContext2D
        gc.clearRect(0.0, 0.0, this.width, this.height)
        gc.stroke = Color.web(if (Settings.noteTheme == Settings.NOTE_THEME_DARK) "#6D79AB" else "#424b73")
        gc.lineWidth = 0.3

        var x = 0.0
        while (x <= this.width) {
            gc.strokeLine(x, 0.0, x, this.height)
            x += gap
        }

        if (lattice) {
            var y = 0.0
            while (y <= this.height) {
                gc.strokeLine(0.0, y, this.width, y)
                y += gap
            }
        }
    }

}