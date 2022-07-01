package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Manges graphical assets for Game list items
 */
public class GameListItem extends ComponentGUI {
    @FXML
    private Text name;
    @FXML
    private ImageView numPlayersImage;
    @FXML
    private Text mode;

    /**
     * Constructor: loads the fxml for game list item
     * @param info info of all the games created
     */
    public GameListItem(GameListInfo info) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/gameListItem.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
        name.setText(info.getGameName());
        if (info.getNumPlayers() == 2) {
            numPlayersImage.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/num_players_2.png"))));
        } else {
            numPlayersImage.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/num_players_3.png"))));
        }
        if (info.getGameMode() == GameMode.BASIC)
            mode.setText("BASIC");
        else
            mode.setText("EXPERT");
    }
}