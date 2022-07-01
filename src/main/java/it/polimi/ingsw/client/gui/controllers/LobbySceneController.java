package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.components.GameListItem;
import it.polimi.ingsw.network.messages.AskGameListMessage;
import it.polimi.ingsw.network.messages.NewGameMessage;
import it.polimi.ingsw.network.messages.SelectGameMessage;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.InputValidator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Controller of lobby scene
 */
public class LobbySceneController extends SceneController {
    @FXML
    public ListView<GameListInfo> gameListView;
    @FXML
    public TextField newGameNameText;
    @FXML
    public Button newGameButton;
    @FXML
    public Text noMatchesText;
    @FXML
    public Button enterButton;
    @FXML
    ListView<Integer> numPlayersListView;
    @FXML
    ListView<GameMode> gameModeListView;
    private boolean isWaitingForMatches;

    /**
     * Method used by the fxml loader to initialise lobby scene
     */
    @FXML
    public void initialize() {
        gameListView.setCellFactory(param -> new ListCell<GameListInfo>() {
            @Override
            public void updateItem(GameListInfo gameInfo, boolean empty) {
                super.updateItem(gameInfo, empty);
                if (gameInfo != null)
                    setGraphic(new GameListItem(gameInfo).getRoot());
            }
        });
        enterButton.setDisable(true);
        gameListView.getSelectionModel().selectedItemProperty().addListener((observable, odlValue, newValue) ->
                enterButton.setDisable(newValue == null)
        );

        numPlayersListView.setItems(FXCollections.observableArrayList(2, 3));
        numPlayersListView.setCellFactory(param -> new ListCell<Integer>() {
            private final ImageView imageView = new ImageView();
            @Override
            public void updateItem(Integer num, boolean empty) {
                super.updateItem(num, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if(num == 2)
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/num_players_2.png"))));
                    else
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/num_players_3.png"))));
                    imageView.setFitWidth(50.0);
                    imageView.setFitHeight(50.0);
                    setGraphic(imageView);
                }
                setText(null);
            }
        });
        gameModeListView.setItems(FXCollections.observableArrayList(GameMode.BASIC, GameMode.EXPERT));
        gameModeListView.setCellFactory(param -> new ListCell<GameMode>() {
            @Override
            public void updateItem(GameMode mode, boolean empty) {
                super.updateItem(mode, empty);
                if (empty)
                    setText(null);
                else
                    setText(mode == GameMode.BASIC ? "BASIC" : "EXPERT");
            }
        });
        newGameButton.setDisable(true);
        newGameNameText.textProperty().addListener((observable, oldValue, newValue) ->
                newGameButton.setDisable(enterIsDisable())
        );
        numPlayersListView.getSelectionModel().selectedItemProperty().addListener((observable, odlValue, newValue) ->
                newGameButton.setDisable(enterIsDisable())
        );
        gameModeListView.getSelectionModel().selectedItemProperty().addListener((observable, odlValue, newValue) ->
                newGameButton.setDisable(enterIsDisable())
        );
    }

    /**
     * Checks if is necessary to disable enter button
     * @return true if not necessary to disable enter button, otherwise false
     */
    private boolean enterIsDisable() {
        Boolean nameIsOk = InputValidator.isWordNotEmpty(newGameNameText.getText());
        Boolean numPlayersIsSelected = numPlayersListView.getSelectionModel().getSelectedItem() != null;
        Boolean gameModeIsSelected = gameModeListView.getSelectionModel().getSelectedItem() != null;
        return !(nameIsOk && numPlayersIsSelected && gameModeIsSelected);
    }

    /**
     * Forcing the alert pane to close and shows loading messages for matches
     * Activates lobby scene
     */
    @Override
    public void activate(){
        alertPaneController.closeAlertPane(true);
        alertPaneController.showLoading("Loading matches");
        isWaitingForMatches = true;
        newGameNameText.setText("");
        super.activate();
    }

    /**
     * Shows messages to notify that is impossible to join the game
     */
    public void gameAlreadyFullHandler() {
        alertPaneController.showError("The game is already full");
    }

    /**
     * Shows the list of matches already created
     * @param list list of matches already created
     */
    public void gameListHandler(List<GameListInfo> list) {
        if (isWaitingForMatches) {
            alertPaneController.closeAlertPane(false);
            isWaitingForMatches = false;
        }
        gameListView.setItems(FXCollections.observableList(list));
        noMatchesText.setVisible(list.isEmpty());
    }

    /**
     * Shows error messages in case the chosen name it's not available
     */
    public void newGameNameHandler() {
        alertPaneController.showError("The name was already chosen");
    }

    /**
     * When player clicks on create button to create new game, the information about the game are send to the server
     * In case all the info are correct shows loading message, otherwise shows error message
     */
    public void onNewMatchClicked() {
        if (!enterIsDisable() && InputValidator.isWordNotBig(newGameNameText.getText())) {
            GameListInfo gameInfo = new GameListInfo(
                    newGameNameText.getText(),
                    gameModeListView.getSelectionModel().getSelectedItem(),
                    numPlayersListView.getSelectionModel().getSelectedItem()
            );
            data.setGameInfo(gameInfo);
            GUIView.getServerHandler().send(new NewGameMessage(GUIView.getPlayerId(), gameInfo));
            alertPaneController.showLoading("Creating game " + gameInfo.getGameName());
        } else if(!enterIsDisable()) {
            alertPaneController.showError("The game name should contain max " + InputValidator.getMaxChars() + " chars");
        } else {
            alertPaneController.showError("Invalid new game values");
        }
    }

    /**
     * When player clicks on update button, client asks the list of matches to the server
     */
    public void onUpdateClicked() {
        alertPaneController.showLoading("Loading matches");
        isWaitingForMatches = true;
        GUIView.getServerHandler().send(new AskGameListMessage(GUIView.getPlayerId()));
    }

    /**
     * When players click on enter button the client send player's info and game's name to the server
     * If everything is ok shows loading message to notify that player will join the game
     * otherwise shows error message
     */
    public void onEnterClicked() {
        GameListInfo gameInfo = gameListView.getSelectionModel().getSelectedItem();
        if (gameInfo != null){
            data.setGameInfo(gameInfo);
            GUIView.getServerHandler().send(new SelectGameMessage(GUIView.getPlayerId(),gameInfo.getGameName()));
            alertPaneController.showLoading("Joining match " + gameInfo.getGameName());
        } else {
            alertPaneController.showError("Invalid choice");
        }
    }
}
