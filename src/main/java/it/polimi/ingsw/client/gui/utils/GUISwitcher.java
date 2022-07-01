package it.polimi.ingsw.client.gui.utils;


import it.polimi.ingsw.client.gui.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Manages the switching of stage
 */
public class GUISwitcher {
    private static GUISwitcher instance = null;
    private Stage primaryStage;
    private HomeSceneController homeSceneController;
    private SettingsSceneController settingsSceneController;
    private DisconnectedSceneController disconnectedSceneController;
    private LobbySceneController lobbySceneController;
    private PlayerInfoSceneController playerInfoSceneController;
    private GameMainSceneController gameMainSceneController;
    private WinningSceneController winningSceneController;
    private SceneController lastController;

    /**
     * Constructor: builds gui switcher
     */
    private GUISwitcher(){
    }

    /**
     * Creates a new instance of gui switcher if it doesn't exist, otherwise return the existing one
     * @return the instance of gui switcher
     */
    public static GUISwitcher getInstance() {
        if (instance == null)
            instance = new GUISwitcher();
        return instance;
    }

    /**
     * Gets the active stage
     * @return the active stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Gets controller for the Home Scene
     * @return controller of Home scene
     */
    public HomeSceneController getHomeSceneController() {
        return homeSceneController;
    }

    /**
     * Gets controller for the Settings Scene
     * @return controller of Settings Scene
     */
    public SettingsSceneController getSettingsSceneController() {
        return settingsSceneController;
    }

    /**
     * Gets controller for the Disconnected Scene
     * @return controller of Disconnected Scene
     */
    public DisconnectedSceneController getDisconnectedSceneController() {
        return disconnectedSceneController;
    }

    /**
     * Gets controller for the Lobby Scene
     * @return controller of Lobby Scene
     */
    public LobbySceneController getLobbySceneController() {
        return lobbySceneController;
    }

    /**
     * Gets controller for the Player Info Scene
     * @return controller of Player Info Scene
     */
    public PlayerInfoSceneController getPlayerInfoSceneController() {
        return playerInfoSceneController;
    }

    /**
     * Gets controller for the Game Main Scene
     * @return controller for the Game Main Scene
     */
    public GameMainSceneController getGameMainSceneController() {
        return gameMainSceneController;
    }

    /**
     * Gets controller for the Winning Scene
     * @return controller for the Winning Scene
     */
    public WinningSceneController getWinningSceneController() {
        return winningSceneController;
    }

    /**
     * Initialises the switcher class by loading each scene's controller
     * @param primaryStage active stage
     */
    public void initialise(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.homeSceneController = (HomeSceneController) loadController("homeScene");
        this.settingsSceneController = (SettingsSceneController) loadController("settingsScene");
        this.disconnectedSceneController = (DisconnectedSceneController) loadController("disconnectedScene");
        this.lobbySceneController = (LobbySceneController) loadController("lobbyScene");
        this.playerInfoSceneController = (PlayerInfoSceneController) loadController("playerInfoScene");
        this.gameMainSceneController = (GameMainSceneController) loadController("gameMainScene");
        this.winningSceneController = (WinningSceneController) loadController("winningScene");
    }

    /**
     * Loads scene and its controller
     * @param fxmlFileName File's name of the scene to active
     * @return controller of active scene
     */
    private SceneController loadController(String fxmlFileName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/" + fxmlFileName + ".fxml"));
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

    /**
     * Handles the switching between scenes
     * @param next controller of the scene to activate
     */
    public void changeController(SceneController next){
        if (lastController != null)
            lastController.deactivate();
        lastController = next;
        next.activate();
    }

    /**
     * Sets default controller
     */
    public void setDefaultController() {
        changeController(homeSceneController);
    }
}
