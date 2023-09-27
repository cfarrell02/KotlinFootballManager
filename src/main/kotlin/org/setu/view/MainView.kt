package org.setu.view

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import tornadofx.*


class MainView : View("League Manager") {
    override val root: Pane = FXMLLoader.load<Pane>(javaClass.getResource("/fxml/MainView.fxml"))
}


