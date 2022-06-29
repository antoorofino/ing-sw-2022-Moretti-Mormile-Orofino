package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class WinningSceneController extends SceneController {
    @FXML
    public ImageView loseImage;
    @FXML
    public ImageView winImage;
    @FXML
    public Text messageText;

    /**
     * action called when click on icon close
     * reset data and switch to home scene
     */
    public void onCloseAlertClicked() {
        GUISwitcher.getInstance().initialise(GUISwitcher.getInstance().getPrimaryStage());
        GUISwitcher.getInstance().setDefaultController();
    }

    /**
     *
     * @param nickname
     */
    public void endMatchHandler(String nickname) {
        if (nickname != null && nickname.equals(ClientData.getInstance().getPlayer().getNickname())) {
            loseImage.setVisible(false);
            winImage.setVisible(true);
            messageText.setText("You won!");
        } else if (nickname != null){
            loseImage.setVisible(true);
            winImage.setVisible(false);
            messageText.setText(nickname + " won");
        } else {
            loseImage.setVisible(false);
            winImage.setVisible(false);
            messageText.setText("Unfortunately a winner could not be decided");
        }
        ensureActive();
    }
}
