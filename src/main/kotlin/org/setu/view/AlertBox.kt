package org.setu.view


import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType


object AlertBox {
    // https://code.makery.ch/blog/javafx-dialogs-official/

    fun display(title: String?, message: String?, buttonText : String = "Ok") {
        val button = ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE)
        val alert = Alert(Alert.AlertType.INFORMATION, message, button )
        alert.title = title
        alert.headerText = null
        alert.showAndWait()
    }

    fun displayConfirmation(title: String?, header: String?, content: String = "", yesButton: String = "Ok",
                            noButton: String = "Cancel"): Boolean {

        val ok = ButtonType(yesButton, ButtonBar.ButtonData.OK_DONE)
        val cancel = ButtonType(noButton, ButtonBar.ButtonData.CANCEL_CLOSE)
        val alert = Alert(Alert.AlertType.CONFIRMATION, content, ok, cancel)
        alert.title = title
        alert.headerText = header
        val result = alert.showAndWait()
        return result.get() == ok
    }



}