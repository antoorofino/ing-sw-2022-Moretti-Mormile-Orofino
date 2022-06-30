package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class CreditsPaneController {
    @FXML
    AnchorPane rootPane;

    @FXML
    public void initialize() {
        rootPane.setVisible(false);
    }

    public void showCredits() {
        rootPane.setVisible(true);
    }

    @FXML
    public void onCloseInfoClicked() {
        rootPane.setVisible(false);
    }
}
