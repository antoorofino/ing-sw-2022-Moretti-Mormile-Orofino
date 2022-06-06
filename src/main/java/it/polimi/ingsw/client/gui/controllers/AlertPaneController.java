package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.DelayAction;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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

    public void closeAlertPane(Boolean force) {
        if (force) {
            alertPane.setVisible(false);
            return;
        }
        DelayAction.executeLater(() -> {
            alertPane.setVisible(false);
        });
    }

    public void showError(String errorMessage) {
        alertPane.setVisible(false);
        alertClose.setVisible(true);
        loadingImage.setVisible(false);
        errorImage.setVisible(true);
        alertText.setText(errorMessage);
        alertPane.setVisible(true);
    }

    public void showLoading(String loadingMessage) {
        alertPane.setVisible(false);
        alertClose.setVisible(false);
        loadingImage.setVisible(true);
        errorImage.setVisible(false);
        alertText.setText(loadingMessage);
        alertPane.setVisible(true);
    }

    public void onCloseAlertClicked() {
        closeAlertPane(true);
    }
}
