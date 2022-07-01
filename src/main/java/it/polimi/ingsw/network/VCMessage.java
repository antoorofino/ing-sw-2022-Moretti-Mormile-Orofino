package it.polimi.ingsw.network;

import it.polimi.ingsw.server.GameController;

/**
 * Message from View to Controller
 */
public interface VCMessage {

    /**
     * Executes the request server-side
     * @param controller The recipient component
     */
    void execute(GameController controller);
}
