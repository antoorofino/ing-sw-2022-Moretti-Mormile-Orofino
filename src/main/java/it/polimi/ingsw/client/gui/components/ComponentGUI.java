package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.ClientData;
import javafx.scene.Parent;

//todo complete java doc
/**
 *
 */
public abstract class ComponentGUI {
    protected Parent root;
    protected final ClientData data = ClientData.getInstance();

    /**
     * Gets the root
     * @return root
     */
    public Parent getRoot() {
        return root;
    }
}
