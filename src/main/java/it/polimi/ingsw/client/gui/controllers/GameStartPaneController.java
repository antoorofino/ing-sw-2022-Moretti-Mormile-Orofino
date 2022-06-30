package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.components.PlayerStartPane;
import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import it.polimi.ingsw.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Controller for pane that explain all players are ready to play
 */
public class GameStartPaneController {
    @FXML
    public AnchorPane alertPane;
    @FXML
    public Label gameNameLabel;
    @FXML
    public HBox containerHBox;
    @FXML
    public Button goToMatchButton;
    private final ClientData data = ClientData.getInstance();

    /**
     * Shows pane that explain all players are ready to play
     */
    public void showGameStart() {
        containerHBox.getChildren().clear();
        gameNameLabel.setText("Game " + data.getGame().getGameName() + " is ready to start");
        Player currentPlayer = data.getPlayer();
        PlayerStartPane p = new PlayerStartPane();
        p.setPlayerInfo(currentPlayer, currentPlayer.getId().equals(GUIView.getPlayerId()));
        containerHBox.getChildren().add(p.getRoot());
        for (Player player : data.getGame().getPlayerHandler().getPlayers()) {
            if (!player.getId().equals(currentPlayer.getId())) {
                p = new PlayerStartPane();
                p.setPlayerInfo(player, player.getId().equals(GUIView.getPlayerId()));
                containerHBox.getChildren().add(p.getRoot());
            }
        }
        alertPane.setVisible(true);
    }

    /**
     * Method used by the fxml loader to initialise Game main scene
     */
    @FXML
    public void initialize() {
        goToMatchButton.setOnMouseClicked(e -> {
            GUISwitcher.getInstance().getGameMainSceneController().ensureActive();
        });
    }

    /**
     * Closes pane
     */
    public void close() {
        alertPane.setVisible(false);
    }
}
