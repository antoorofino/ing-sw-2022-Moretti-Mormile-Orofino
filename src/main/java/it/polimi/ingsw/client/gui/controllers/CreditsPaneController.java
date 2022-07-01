package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * Controller of Credits pane
 */
public class CreditsPaneController {
    @FXML
    AnchorPane rootPane;

    /**
     * Method used by the fxml loader to initialise the credits pane
     */
    @FXML
    public void initialize() {
        rootPane.setVisible(false);
    }

    /**
     * Shows credits pane
     */
    public void showCredits() {
        rootPane.setVisible(true);
    }

    /**
     * When players clicks on close icon credits pane will be hided
     */
    @FXML
    public void onCloseInfoClicked() {
        rootPane.setVisible(false);
    }
}
