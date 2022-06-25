package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
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
    private final ClientData clientData = ClientData.getInstance();
    private boolean nicknameIsSet;

    @FXML
    public void initialize() {
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

    private boolean applyIsDisable() {
        Boolean nicknameIsOk = InputValidator.isWordNotEmpty(nicknameText.getText());
        Boolean colorSelected = towerListView.getSelectionModel().getSelectedItem() != null;
        if (!nicknameIsSet)
            return !nicknameIsOk;
        return !(nicknameIsOk && colorSelected);
    }

    public void askNicknameHandler(Boolean isFirstRequest) {
        if(!isFirstRequest)
            alertPaneController.showError("Nickname already chosen");
        ensureActive();
    }

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
        if (clientData.getGameInfo().getNumPlayers() == 3) {
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
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_black.png"))));
                    else if (color == TowerColor.WHITE)
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_white.png"))));
                    else
                        imageView.setImage(new Image(String.valueOf(getClass().getResource("/gui/images/tower_grey.png"))));
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

    public void showQueuing() {
        alertPaneController.showLoading("Waiting for other players to connect");
    }

    public void showGameStart() {
        alertPaneController.closeAlertPane(true);
        gameStartPaneController.showGameStart();
    }

    public void onApplyClick() {
        if (!applyIsDisable()) {
            String nickname = nicknameText.getText();
            TowerColor color = towerListView.getSelectionModel().getSelectedItem();
            if (!nicknameIsSet) {
                GUIView.getServerHandler().send(new SetNickname(GUIView.getPlayerId(), nickname));
                alertPaneController.showLoading("Setting nickname");
            } else {
                GUIView.getServerHandler().send(new SetTowerColor(GUIView.getPlayerId(), color));
                alertPaneController.showLoading("Setting tower color");
            }
        } else {
            alertPaneController.showError("Invalid values");
        }
    }
}
