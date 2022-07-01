package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * Controller for winning scene
 */
public class WinningSceneController extends SceneController {
    @FXML
    public ImageView loseImage;
    @FXML
    public ImageView winImage;
    @FXML
    public Text messageText;

    /**
     * Action called when click on icon close
     * reset data and switch to home scene
     */
    public void onCloseAlertClicked() {
        switcher.initialise(switcher.getPrimaryStage());
        switcher.setDefaultController();
        data.resetPossibleActions();
    }

    //TODO: check java doc
    /**
     * Shows winning or loosing message based on player
     * @param nickname player's nickname
     */
    public void endMatchHandler(String nickname) {
        if (nickname != null && nickname.equals(data.getPlayer().getNickname())) {
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
