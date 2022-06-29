package it.polimi.ingsw.client.gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

public class AssistantCardGUI extends ComponentGUI {
    @FXML
    private Pane assistantImagePane;
    @FXML
    private Pane assistantBlurPane;
    @FXML
    private HBox messageHBox;
    @FXML
    private Label messageLabel;
    private final static String selectedClassName = "assistant-selected";
    private final static String notAvailableClassName = "assistant-not-available";
    private final static String notAvailableMessageText = "Already played";
    private final static String alreadyPlayedClassName = "assistant-already-played";
    private final static String alreadyPlayedMessageText = "Not available";
    private Consumer<Integer> listener;

    public AssistantCardGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/fxml/assistantCard.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException ignored) {
        }
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

    public void setPlayedByNickname(String nickname) {
        assistantBlurPane.setVisible(true);
        messageLabel.setText(nickname);
        messageLabel.setVisible(true);
        messageLabel.setTranslateY(200.00);
        messageHBox.setVisible(true);
    }
}
