package org.setu.view

import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.layout.Pane
import org.setu.controller.MainController
import tornadofx.View


class MainView : View("League Manager") {
    private var loader = FXMLLoader(javaClass.getResource("/fxml/MainView.fxml"))
    override var root: Parent = loader.load<Parent>()
    private var controller: MainController = loader.getController<MainController>()

    init {
        primaryStage.isResizable = false
        primaryStage.setOnCloseRequest {

            if (controller.isDirty && AlertBox.displayConfirmation(
                    "Save?",
                    "Would you like to save before exiting?", yesButton = "Yes", noButton = "No"
                )
            ) {
                controller.save()
            }
        }

    }
}
