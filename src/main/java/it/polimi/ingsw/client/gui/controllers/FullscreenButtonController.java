package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FullscreenButtonController {
    @FXML
    StackPane pane;
    @FXML
    public ImageView buttonEnter;
    @FXML
    public ImageView buttonExit;

    @FXML
    public void initialize(){
        buttonEnter.fitWidthProperty().bind(pane.prefWidthProperty());
        buttonEnter.fitHeightProperty().bind(pane.prefHeightProperty());
        buttonExit.fitWidthProperty().bind(pane.prefWidthProperty());
        buttonExit.fitHeightProperty().bind(pane.prefHeightProperty());

        Stage primaryStage = GUISwitcher.getInstance().getPrimaryStage();

        primaryStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            buttonEnter.setVisible(!newValue);
            buttonExit.setVisible(newValue);
        });

        buttonEnter.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            primaryStage.setFullScreen(true);
        });

        buttonExit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            primaryStage.setFullScreen(false);
        });
    }
}
