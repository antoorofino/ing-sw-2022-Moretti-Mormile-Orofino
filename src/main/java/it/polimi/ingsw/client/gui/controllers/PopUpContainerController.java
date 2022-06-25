package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class PopUpContainerController {
    @FXML
    public AnchorPane popUpPane;
    @FXML
    public VBox rootPane;
    @FXML
    public ImageView popUpClose;

    @FXML
    public void initialize() {
        popUpPane.setVisible(false);
        popUpClose.setOnMouseClicked(e -> popUpPane.setVisible(false));
    }

    public void display() {
        popUpPane.setVisible(true);
    }

    public void clear() {
        rootPane.getChildren().clear();
        popUpPane.setVisible(false);
    }

    public void add(Parent root) {
        rootPane.getChildren().add(root);
    }
}
