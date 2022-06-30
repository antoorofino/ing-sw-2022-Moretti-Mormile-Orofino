package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * Controller of Credits pane
 */
public class CreditsPaneController {
    @FXML
    AnchorPane rootPane;
    //TODO: check javadoc
    /**
     * Method used by the fxml loader to initialise the pane where player can see credits about developer
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
     * When players click on close icon credits pane will be not visible
     */
    @FXML
    public void onCloseInfoClicked() {
        rootPane.setVisible(false);
    }
}
