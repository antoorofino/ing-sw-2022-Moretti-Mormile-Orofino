package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for pop up container
 */
public class PopUpContainerController {
    @FXML
    public AnchorPane popUpPane;
    @FXML
    public VBox rootPane;
    @FXML
    public ImageView popUpClose;

    /**
     * Method used by the fxml loader to initialise container
     */
    @FXML
    public void initialize() {
        popUpPane.setVisible(false);
        popUpClose.setOnMouseClicked(e -> popUpPane.setVisible(false));
    }

    /**
     * Shows pop up
     */
    public void display() {
        popUpPane.setVisible(true);
    }

    /**
     * Resets pop up and sets invisible
     */
    public void clear() {
        rootPane.getChildren().clear();
        popUpPane.setVisible(false);
    }

    //TODO: complete javadoc
    /**
     *
     * @param root
     */
    public void add(Parent root) {
        rootPane.getChildren().add(root);
    }
}
