package org.setu.view

import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import tornadofx.*


class MainView : View("League Manager") {
    override val root: Pane = FXMLLoader.load<Pane>(javaClass.getResource("/fxml/MainView.fxml"))
}


