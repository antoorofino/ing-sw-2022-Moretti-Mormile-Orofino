package it.polimi.ingsw.client.gui.components;

import it.polimi.ingsw.client.gui.utils.ClientData;
import javafx.scene.Parent;

public abstract class ComponentGUI {
    protected Parent root;
    protected ClientData data = ClientData.getInstance();

    public Parent getRoot() {
        return root;
    }
}
