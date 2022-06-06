package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.InputValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Objects;

public class SettingsSceneController extends SceneController {
    private final ClientData data = ClientData.getInstance();
    @FXML
    public Button applyButton;
    @FXML
    private TextField ipText;
    @FXML
    private TextField portText;

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
        if (!Objects.equals(data.getIpAddress(), Configurator.getServerIp()))
            ipText.setText(data.getIpAddress());
        if (data.getPortNumber() != Configurator.getServerPort())
            portText.setText(String.valueOf(data.getPortNumber()));
        super.activate(); //Then continue with default behaviour
    }

    public void onApplyClick() {
        boolean incorrect = false;
        String serverIp = null;
        int serverPort = 0;

        if (ipText.getText().isEmpty()){
            serverIp = Configurator.getServerIp();
        } else if (InputValidator.isIp(ipText.getText())) {
            serverIp = ipText.getText();
        } else {
            incorrect = true;
        }

        if (portText.getText().isEmpty()){
            serverPort = Configurator.getServerPort();
        } else if (InputValidator.isPortNumber(portText.getText())) {
            serverPort = Integer.parseInt(portText.getText());
        } else {
            incorrect = true;
        }

        if(incorrect) {
            alertPaneController.showError("Wrong valid for server ip or port number");
        } else {
            data.setIpAddress(serverIp);
            data.setPortNumber(serverPort);
            alertPaneController.closeAlertPane(true);
            GUISwitcher.getInstance().setDefaultController();
        }
    }

    public void onBackClick() {
        ipText.setText("");
        portText.setText("");
        alertPaneController.closeAlertPane(true);
        GUISwitcher.getInstance().setDefaultController();
    }
}

