package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class SceneController {
    private Parent root;
    private boolean isActive;
    public AlertPaneController alertPaneController;

    public void init(Parent root) {
        this.root = root;
    }

    public void activate(){
        GUISwitcher.getInstance().getPrimaryStage().getScene().setRoot(root);
        isActive = true;
    }

    public void deactivate(){
        isActive = false;
    }

    public void ensureActive(){
        if (!isActive)
            GUISwitcher.getInstance().changeController(this);
    }
}
