package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import javafx.scene.Parent;

public abstract class SceneController {
    private Parent root;
    private boolean isActive;
    public AlertPaneController alertPaneController;
    protected final ClientData data = ClientData.getInstance();
    protected final GUISwitcher switcher = GUISwitcher.getInstance();

    public void init(Parent root) {
        this.root = root;
    }

    public void activate(){
        switcher.getPrimaryStage().getScene().setRoot(root);
        isActive = true;
    }

    public void deactivate(){
        isActive = false;
    }

    public void ensureActive(){
        if (!isActive)
            switcher.changeController(this);
    }
}
