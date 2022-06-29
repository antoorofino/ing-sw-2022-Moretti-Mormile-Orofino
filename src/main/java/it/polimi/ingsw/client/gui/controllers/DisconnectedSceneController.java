package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.GUISwitcher;
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
        GUISwitcher.getInstance().initialise(GUISwitcher.getInstance().getPrimaryStage());
        GUISwitcher.getInstance().setDefaultController();
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
