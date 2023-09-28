package org.setu.view


import javafx.scene.control.Alert

import javafx.scene.control.ButtonType



object AlertBox {
    // https://code.makery.ch/blog/javafx-dialogs-official/

    fun display(title: String?, message: String?) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }

    fun displayConfirmation(title: String?, header: String?, content: String = ""): Boolean {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = title
        alert.headerText = header
        alert.contentText = content
        val result = alert.showAndWait()
        return result.get() == ButtonType.OK
    }

    
}