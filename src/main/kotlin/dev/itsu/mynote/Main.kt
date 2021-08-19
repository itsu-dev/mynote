package dev.itsu.mynote

import dev.itsu.mynote.data.Settings
import dev.itsu.mynote.ui.BaseComponent
import dev.itsu.mynote.ui.UIManager
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage


class Main : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Mynote for Windows 10"
        primaryStage.scene = UIManager.createScene()

        UIManager.loadCSS()
        UIManager.loadNoteCSS()

        primaryStage.show()
    }

}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}