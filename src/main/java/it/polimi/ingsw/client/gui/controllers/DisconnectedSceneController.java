package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DisconnectedSceneController extends SceneController{
    @FXML
    public Text alertText;

    public void onCloseAlertClicked() {
        GUISwitcher.getInstance().initialise(GUISwitcher.getInstance().getPrimaryStage());
        GUISwitcher.getInstance().setDefaultController();
    }

    public void disconnectedHandler(String errorMessage) {
        alertText.setText(errorMessage);
        ensureActive();
    }
}
