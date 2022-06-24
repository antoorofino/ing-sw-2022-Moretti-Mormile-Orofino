package it.polimi.ingsw.network;

import it.polimi.ingsw.server.GameController;

/**
 * Message from View to Controller
 */
public interface VCMessage {

    /**
     * Execute the request server-side
     * @param controller The recipient component
     */
    void execute(GameController controller);
}
