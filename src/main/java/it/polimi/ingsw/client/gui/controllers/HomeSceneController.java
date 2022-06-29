package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class HomeSceneController extends SceneController{
    @FXML
    public Button playButton;
    @FXML
    public AnchorPane alertPane;

    @FXML
    public void initialize() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000.0), playButton);
        scaleTransition.setByX(0.2);
        scaleTransition.setByY(0.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.play();
    }

    @Override
    public void activate() {
        alertPaneController.closeAlertPane(true);
        super.activate();
    }

    public void onPlayClicked() {
        (new Thread(() -> {
            GUIView.getServerHandler().setConnection(data.getIpAddress(), data.getPortNumber());
        })).start();
        alertPaneController.showLoading("Connecting to the server");
    }

    public void connectionErrorHandler() {
        alertPaneController.showError("Unable to connect to the server. Check ip address and port number in settings");
    }

    public void onCreditsClicked() {
        //TODO: not yet implemented
        alertPaneController.showError("NOT YET IMPLEMENTED");
    }

    public void onSettingsClicked() {
        alertPaneController.closeAlertPane(true);
        GUISwitcher.getInstance().getSettingsSceneController().activate();
    }
}
