package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DisconnectedSceneController extends SceneController{
    @FXML
    public Text alertText;

    /**
     * action called when click on icon close
     * reset data and switch to home scene
     */
    public void onCloseAlertClicked() {
        switcher.initialise(switcher.getPrimaryStage());
        switcher.setDefaultController();
    }

    /**
     * show error message and set
     * @param errorMessage
     */
    public void disconnectedHandler(String errorMessage) {
        alertText.setText(errorMessage);
        ensureActive();
    }
}
