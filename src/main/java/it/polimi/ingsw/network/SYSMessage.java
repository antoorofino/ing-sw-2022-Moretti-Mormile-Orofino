package it.polimi.ingsw.network;

import it.polimi.ingsw.server.ServerMain;

/**
 * System message between Server and Client
 */
public interface SYSMessage {

    /**
     * Executes the request server-side
     * @param serverMain The recipient component
     */
    void execute(ServerMain serverMain);
}
