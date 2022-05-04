package it.polimi.ingsw.network;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.VirtualView;

/**
 * System message between Server and Client
 */
public interface SYSMessage extends Message {

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    void execute(View view);

    /**
     * Execute the request server-side
     *
     * @param virtualView The recipient component
     */
    void execute(VirtualView virtualView);
}
