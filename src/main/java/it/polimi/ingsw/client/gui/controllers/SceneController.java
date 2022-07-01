package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.utils.ClientData;
import it.polimi.ingsw.client.gui.utils.GUISwitcher;
import javafx.scene.Parent;

/**
 * Controller used to activate or deactivate a scene
 */
public abstract class SceneController {
    private Parent root;
    private boolean isActive;
    public AlertPaneController alertPaneController;
    protected final ClientData data = ClientData.getInstance();
    protected final GUISwitcher switcher = GUISwitcher.getInstance();

    /**
     * Saves the root node
     * @param root root node
     */
    public void init(Parent root) {
        this.root = root;
    }

    /**
     * Activates a scene
     */
    public void activate(){
        switcher.getPrimaryStage().getScene().setRoot(root);
        isActive = true;
    }

    /**
     * Deactivates a scene
     */
    public void deactivate(){
        isActive = false;
    }

    /**
     * Sets as default controller this (SceneController)
     */
    public void ensureActive(){
        if (!isActive)
            switcher.changeController(this);
    }
}
