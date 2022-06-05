package it.polimi.ingsw.network;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.server.VirtualView;

/**
 * System message between Server and Client
 */
public interface SYSMessage {

    /**
     * Execute the request server-side
     *
     * @param serverMain The recipient component
     */
    void execute(ServerMain serverMain);
}
