package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.network.messages.SetNickname;
import it.polimi.ingsw.network.messages.SetTowerColor;
import it.polimi.ingsw.util.InputValidator;
import it.polimi.ingsw.util.TowerColor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller for player info scene
 */
public class PlayerInfoSceneController extends SceneController {
    @FXML
    public TextField nicknameText;
    @FXML
    public Button applyButton;
    @FXML
    public VBox askColorPane;
    @FXML
    public ListView<TowerColor> towerListView;
    @FXML
    public GameStartPaneController gameStartPaneController;
    private boolean nicknameIsSet;

    /**
     * Method used by the fxml loader to initialise player info scene
     */
    @FXML
    public void initialize() {
        alertPaneController.closeAlertPane(true);
        gameStartPaneController.close();
        nicknameIsSet = false;
        askColorPane.setVisible(false);
        applyButton.setDisable(true);
        nicknameText.textProperty().addListener((observable, oldValue, newValue) ->
                applyButton.setDisable(applyIsDisable())
        );
        towerListView.getSelectionModel().selectedItemProperty().addListener((observable, odlValue, newValue) ->
                applyButton.setDisable(applyIsDisable())
        );
    }

    /**
     * Checks if is necessary to disable apply button or not
     * @return true if is necessary disable apply button, otherwise false
     */
    private boolean applyIsDisable() {
        Boolean nicknameIsOk = InputValidator.isWordNotEmpty(nicknameText.getText());
        Boolean colorSelected = towerListView.getSelectionModel().getSelectedItem() != null;
        if (!nicknameIsSet)
            return !nicknameIsOk;
        return !(nicknameIsOk && colorSelected);
    }

    /**
     * Handles the AskNickname message
     * @param isFirstRequest if false shows error on nickname already chosen
     */
    public void askNicknameHandler(Boolean isFirstRequest) {
        if(!isFirstRequest)
            alertPaneController.showError("Nickname already chosen");
        ensureActive();
    }

    /**
     * Handles the AskTowerColor message
     * @param possibleColors colors available
     * @param isFirstRequest if false shows error on tower color already chosen
     */
    public void askTowerColor(List<TowerColor> possibleColors, Boolean isFirstRequest) {
        nicknameIsSet = true;
        nicknameText.setDisable(true);
        applyButton.setDisable(true);
        if(!isFirstRequest) {
            alertPaneController.closeAlertPane(true);
            towerListView.getSelectionModel().clearSelection();
            alertPaneController.showError("Color already chosen");
        } else {
            alertPaneController.closeAlertPane(false);
        }
        ObservableList<TowerColor> colors = FXCollections.observableArrayList();
        colors.addAll(TowerColor.BLACK, TowerColor.WHITE);
        if (data.getGameInfo().getNumPlayers() == 3) {
            colors.add(TowerColor.GRAY);
            towerListView.setPrefWidth(275.0);
        }
        towerListView.setItems(colors);
        towerListView.setCellFactory(param -> new ListCell<TowerColor>() {
            private final ImageView imageView = new ImageView();
            @Override
            public void updateItem(TowerColor color, boolean empty) {
                super.updateItem(color, empty);
                if (empty) {
                    setGraphic(null);
                } else if (possibleColors.contains(color)) {
                    if(color == TowerColor.BLACK)
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_black_no_stroke.png"))));
                    else if (color == TowerColor.WHITE)
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_white_no_stroke.png"))));
                    else
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_grey_no_stroke.png"))));
                    imageView.setFitWidth(60.0);
                    imageView.setFitHeight(90.0);
                    setGraphic(imageView);
                } else {
                    if(color == TowerColor.BLACK)
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_black_no_available.png"))));
                    else if (color == TowerColor.WHITE)
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_white_no_available.png"))));
                    else
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_grey_no_available.png"))));
                    imageView.setFitWidth(60.0);
                    imageView.setFitHeight(90.0);
                    setGraphic(imageView);
                    setDisable(true);
                }
                setText(null);
            }
        });
        askColorPane.setVisible(true);
    }

    /**
     * Shows loading messages if player have to wait other player
     */
    public void showQueuing() {
        alertPaneController.showLoading("Waiting for other players to connect");
    }

    /**
     * Shows game start pane to notify that all the players are ready to start the match
     */
    public void showGameStart() {
        alertPaneController.closeAlertPane(true);
        gameStartPaneController.showGameStart();
    }

    /**
     * When player clicks on apply button if there aren't error send all the info about the player to the server
     * Shows message to notify that client is sending info to the server
     * If there are any error shows error message
     */
    public void onApplyClick() {
        if (!applyIsDisable()) {
            String nickname = nicknameText.getText();
            TowerColor color = towerListView.getSelectionModel().getSelectedItem();
            if (!nicknameIsSet && InputValidator.isWordNotBig(nickname)) {
                GUIView.getServerHandler().send(new SetNickname(GUIView.getPlayerId(), nickname));
                alertPaneController.showLoading("Setting nickname");
            } else if (!nicknameIsSet) {
                alertPaneController.showError("The nickname should contain max " + InputValidator.getMaxChars() + " chars");
            } else {
                GUIView.getServerHandler().send(new SetTowerColor(GUIView.getPlayerId(), color));
                alertPaneController.showLoading("Setting tower color");
            }
        } else {
            alertPaneController.showError("Invalid values");
        }
    }
}
