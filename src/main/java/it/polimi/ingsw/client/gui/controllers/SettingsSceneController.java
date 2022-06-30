package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.InputValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Objects;

/**
 * Controller for settings scene
 */
public class SettingsSceneController extends SceneController {
    @FXML
    public Button applyButton;
    @FXML
    private TextField ipText;
    @FXML
    private TextField portText;

    /**
     * Method used by the fxml loader to initialise Game main scene
     */
    @FXML
    public void initialize() {
        ipText.setFocusTraversable(false);
        portText.setFocusTraversable(false);
        ipText.textProperty().addListener((observable, oldValue, newValue) -> {
            applyButton.setDisable(false);
        });
        portText.textProperty().addListener((observable, oldValue, newValue) -> {
            applyButton.setDisable(false);
        });
    }

    /**
     * Override superclass activate to show last IP, Port choice
     */
    @Override
    public void activate() {
        applyButton.setDisable(true);
        if (!Objects.equals(data.getIpAddress(), Configurator.getDefaultServerIp()))
            ipText.setText(data.getIpAddress());
        if (data.getPortNumber() != Configurator.getDefaultServerPort())
            portText.setText(String.valueOf(data.getPortNumber()));
        super.activate(); //Then continue with default behaviour
    }

    /**
     * When player clicks apply button, saves modify about parameters for communication between server and client
     */
    public void onApplyClick() {
        boolean incorrect = false;
        String serverIp = null;
        int serverPort = 0;

        if (ipText.getText().isEmpty()){
            serverIp = Configurator.getDefaultServerIp();
        } else if (InputValidator.isIp(ipText.getText())) {
            serverIp = ipText.getText();
        } else {
            incorrect = true;
        }

        if (portText.getText().isEmpty()){
            serverPort = Configurator.getDefaultServerPort();
        } else if (InputValidator.isPortNumber(portText.getText())) {
            serverPort = Integer.parseInt(portText.getText());
        } else {
            incorrect = true;
        }

        if(incorrect) {
            alertPaneController.showError("Wrong value for server ip or port number");
        } else {
            data.setIpAddress(serverIp);
            data.setPortNumber(serverPort);
            alertPaneController.closeAlertPane(true);
            GUISwitcher.getInstance().setDefaultController();
        }
    }

    /**
     * When player clicks back button, alert pane will be closed and player goes back to home page
     */
    public void onBackClick() {
        ipText.setText("");
        portText.setText("");
        alertPaneController.closeAlertPane(true);
        GUISwitcher.getInstance().setDefaultController();
    }
}

