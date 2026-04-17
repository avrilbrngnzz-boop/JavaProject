package com.example.javaproject;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class DemineurController {

    @FXML
    private GridPane grilleDemineur;

    @FXML
    public void initialize() {
        System.out.println("Le jeu du démineur est lancé !");
    }

    @FXML
    private void handleSaveAndQuit() {
        System.exit(0);
    }
}

