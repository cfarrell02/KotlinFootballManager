package org.setu.view

import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import org.setu.controller.MainController
import tornadofx.*


class MainView : View("League Manager") {
    override val root: Pane = FXMLLoader.load<Pane>(javaClass.getResource("/fxml/MainView.fxml"))

    init {
        primaryStage.isResizable = false

    }


}


