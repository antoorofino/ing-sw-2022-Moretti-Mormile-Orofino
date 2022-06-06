package it.polimi.ingsw.client.gui.utils;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class GUISwitcher {
    private static GUISwitcher instance = null;
    private Stage primaryStage;
    private HomeSceneController homeSceneController;
    private SettingsSceneController settingsSceneController;
    private DisconnectedSceneController disconnectedSceneController;
    private LobbySceneController lobbySceneController;
    private PlayerInfoSceneController playerInfoSceneController;
    private SceneController lastController;
    private GUISwitcher(){
    }

    public static GUISwitcher getInstance() {
        if (instance == null)
            instance = new GUISwitcher();
        return instance;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public HomeSceneController getHomeSceneController() {
        return homeSceneController;
    }

    public SettingsSceneController getSettingsSceneController() {
        return settingsSceneController;
    }

    public DisconnectedSceneController getDisconnectedSceneController() {
        return disconnectedSceneController;
    }

    public LobbySceneController getLobbySceneController() {
        return lobbySceneController;
    }
    public PlayerInfoSceneController getPlayerInfoSceneController() {
        return playerInfoSceneController;
    }

    public void initialise(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.homeSceneController = (HomeSceneController) loadController("homeScene");
        this.settingsSceneController = (SettingsSceneController) loadController("settingsScene");
        this.disconnectedSceneController = (DisconnectedSceneController) loadController("disconnectedScene");
        this.lobbySceneController = (LobbySceneController) loadController("lobbyScene");
        this.playerInfoSceneController = (PlayerInfoSceneController) loadController("playerInfoScene");
    }

    private SceneController loadController(String fxmlFileName) {
        FXMLLoader loader = new FXMLLoader(GUIView.class.getResource("/gui/fxml/" + fxmlFileName + ".fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SceneController controller = loader.getController();
        controller.init(root);
        return controller;
    }

    public void changeController(SceneController next){
        if (lastController != null)
            lastController.deactivate();
        lastController = next;
        next.activate();
    }

    public void setDefaultController() {
        changeController(homeSceneController);
    }
}
