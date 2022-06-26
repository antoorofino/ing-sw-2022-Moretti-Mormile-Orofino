package it.polimi.ingsw.client.gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

public class AssistantCardGUI {
    @FXML
    private Pane assistantImagePane;
    @FXML
    private Pane assistantBlurPane;
    @FXML
    private HBox messageHBox;
    @FXML
    private Label messageLabel;
    private final String selectedClassName = "assistant-selected";
    private final String notAvailableClassName = "assistant-not-available";
    private final String notAvailableMessageText = "Already played";
    private final String alreadyPlayedClassName = "assistant-already-played";
    private final String alreadyPlayedMessageText = "Not available";
    private Consumer<Integer> listener;
    private Parent root;

    public AssistantCardGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/assistantCard.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
    }

    public Parent getRoot() {
        return root;
    }

    @FXML
    public void initialize() {
        // Set default values
        assistantImagePane.getStyleClass().clear();
        assistantBlurPane.getStyleClass().clear();
        assistantBlurPane.setVisible(false);
        messageHBox.setVisible(false);
        messageLabel.setVisible(false);
    }

    public void initialize(int id) {
        initialize();
        assistantImagePane.getStyleClass().add("assistant-" + id);
        assistantImagePane.setOnMouseClicked(e -> listener.accept(id));
    }

    public void select() {
        assistantImagePane.getStyleClass().add(selectedClassName);
    }

    public void deselect() {
        assistantImagePane.getStyleClass().remove(selectedClassName);
    }

    public void setOnClickedListener(Consumer<Integer> listener) {
        this.listener = listener;
    }

    public void setAlreadyPlayed() {
        assistantBlurPane.setVisible(true);
        assistantBlurPane.getStyleClass().add(alreadyPlayedClassName);
        messageLabel.setText(alreadyPlayedMessageText);
        messageLabel.setVisible(true);
        messageHBox.setVisible(true);
    }

    public void setNotAvailable() {
        assistantBlurPane.setVisible(true);
        assistantBlurPane.getStyleClass().add(notAvailableClassName);
        messageLabel.setText(notAvailableMessageText);
        messageLabel.setVisible(true);
        messageHBox.setVisible(true);
    }
}
