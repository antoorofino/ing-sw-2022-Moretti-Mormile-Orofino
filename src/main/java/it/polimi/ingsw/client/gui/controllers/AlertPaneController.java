package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.DelayAction;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Controller of alert pane
 */
public class AlertPaneController {
    @FXML
    public AnchorPane alertPane;
    @FXML
    public ImageView loadingImage;
    @FXML
    public ImageView errorImage;
    @FXML
    public Text alertText;
    @FXML
    public ImageView alertClose;

    /**
     * Closes  alert pane
     * @param force explain if is necessary to close alert pane
     */
    public void closeAlertPane(Boolean force) {
        if (force) {
            alertPane.setVisible(false);
            return;
        }
        DelayAction.executeLater(() -> {
            alertPane.setVisible(false);
        });
    }

    /**
     * Shows panel for error
     * @param errorMessage error message
     */
    public void showError(String errorMessage) {
        alertPane.setVisible(false);
        alertClose.setVisible(true);
        loadingImage.setVisible(false);
        errorImage.setVisible(true);
        alertText.setText(errorMessage);
        alertPane.setVisible(true);
    }

    /**
     * Shows loading pane
     * @param loadingMessage loading message
     */
    public void showLoading(String loadingMessage) {
        alertPane.setVisible(false);
        alertClose.setVisible(false);
        loadingImage.setVisible(true);
        errorImage.setVisible(false);
        alertText.setText(loadingMessage);
        alertPane.setVisible(true);
    }

    /**
     * Forcing close alert pane, the method is called when click on the icon that close alert pane
     */
    public void onCloseAlertClicked() {
        closeAlertPane(true);
    }
}
