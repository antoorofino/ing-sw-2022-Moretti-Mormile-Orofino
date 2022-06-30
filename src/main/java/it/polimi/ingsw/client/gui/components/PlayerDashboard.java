package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.GameMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * Manges graphical assets and event in dash board
 */
public class PlayerDashboard extends ComponentGUI {
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView towerImageView;
    @FXML
    private Label towerLabel;
    @FXML
    private HBox coinHBox;
    @FXML
    private Label coinLabel;

    /**
     * Constructor: load the fxml for the player's dashboard
     */
    public PlayerDashboard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/playerDashboard.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void initialize() {
        coinHBox.setVisible(false);
    }

    /**
     * Sets graphical assets to show player's info
     * @param player player
     * @param mode game mode
     */
    public void setPlayerInfo(Player player, GameMode mode) {
        playerNameLabel.setText(player.getNickname());
        Image image;
        switch (player.getTowerColor()) {
            case GRAY:
                image = new Image(String.valueOf(getClass().getResource("/gui/images/tower_grey_no_stroke.png")));
                break;
            case BLACK:
                image = new Image(String.valueOf(getClass().getResource("/gui/images/tower_black_no_stroke.png")));
                break;
            case WHITE:
                image = new Image(String.valueOf(getClass().getResource("/gui/images/tower_white_no_stroke.png")));
                break;
            default:
                throw new RuntimeException("Invalid tower color");
        }
        towerImageView.setImage(image);
        towerLabel.setText(String.valueOf(player.getNumOfTowers()));
        coinHBox.setVisible(mode == GameMode.EXPERT);
        coinLabel.setText(String.valueOf(player.getCoin()));
    }
}
