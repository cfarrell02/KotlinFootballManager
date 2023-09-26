package org.setu.controller

import javafx.fxml.FXML
import javafx.scene.control.ListView

class MainController {
    @FXML
    lateinit var mainList: ListView<String>  // <--- This is the ListView defined in the FXML file

    @FXML
    fun initialize() {
        mainList.items.add("Hello")
        mainList.items.add("World")
    }
}