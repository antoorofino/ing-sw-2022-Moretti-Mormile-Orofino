package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * Controller for scene that will be visible in case of disconnections
 */
public class DisconnectedSceneController extends SceneController{
    @FXML
    public Text alertText;

    /**
     * Action called when player clicks on icon close
     * Resets data and switch to home scene
     */
    public void onCloseAlertClicked() {
        switcher.initialise(switcher.getPrimaryStage());
        switcher.setDefaultController();
        data.resetPossibleActions();
    }

    /**
     * Shows error message
     * @param errorMessage error message
     */
    public void disconnectedHandler(String errorMessage) {
        alertText.setText(errorMessage);
        ensureActive();
    }
}
