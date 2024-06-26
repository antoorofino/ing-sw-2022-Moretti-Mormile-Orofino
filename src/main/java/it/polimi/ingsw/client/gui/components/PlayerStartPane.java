package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Manages graphical assets and event in player start pane
 */
public class PlayerStartPane extends ComponentGUI {
    @FXML
    private Label playerNameLabel;
    @FXML
    private Pane towerImagePane;

    /**
     * Constructor: loads the fxml for the player start pane
     */
    public PlayerStartPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/playerStartPane.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    /**
     * Associates each player to the selected tower's color
     * @param player player
     * @param isCurrentPlayer if it's true it's necessary to print 'you' otherwise player's nickname
     */
    public void setPlayerInfo(Player player, boolean isCurrentPlayer) {
        if (isCurrentPlayer)
            playerNameLabel.setText("You");
        else
            playerNameLabel.setText(player.getNickname());
        towerImagePane.getStyleClass().clear();
        switch (player.getTowerColor()) {
            case BLACK:
                towerImagePane.getStyleClass().add("tower-black-background-no-stroke");
                break;
            case WHITE:
                towerImagePane.getStyleClass().add("tower-white-background-no-stroke");
                break;
            case GRAY:
                towerImagePane.getStyleClass().add("tower-grey-background-no-stroke");
                break;
        }
    }
}
