package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.ClientData;
import javafx.scene.Parent;

/**
 * Class from each every GUI component inherit
 */
public abstract class ComponentGUI {
    protected Parent root;
    protected final ClientData data = ClientData.getInstance();

    /**
     * Gets the root node
     * @return root node
     */
    public Parent getRoot() {
        return root;
    }
}
