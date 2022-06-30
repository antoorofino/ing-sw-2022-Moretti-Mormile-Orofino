package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TeachersHandler;
import it.polimi.ingsw.util.GameMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

//TODO: java doc

public class BoardPopUp extends ComponentGUI {
    @FXML
    private Pane dashboardPane;
    @FXML
    private Pane boardPane;

    /**
     * Constructor: load the fxml for the Board pop up
     */
    public BoardPopUp() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/boardsPopUp.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    /**
     * Sets graphical components specific for a player
     * @param player player
     * @param mode game mode
     * @param handler teacher handler
     */
    public void setPlayerInfo(Player player, GameMode mode, TeachersHandler handler) {
        // Set player dashboard
        PlayerDashboard dashboard = new PlayerDashboard();
        dashboard.setPlayerInfo(player, mode);
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(dashboard.getRoot());
        // Set player board
        PlayerBoard board = new PlayerBoard();
        board.setBoard(player.getPlayerBoard(), handler.getTeachersByPlayerId(player.getId()), false, null);
        boardPane.getChildren().clear();
        boardPane.getChildren().add(board.getRoot());
    }
}
