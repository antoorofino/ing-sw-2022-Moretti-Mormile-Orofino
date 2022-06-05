package it.polimi.ingsw.network;

import it.polimi.ingsw.client.View;

/**
 * Message from Controller to View
 */
public interface CVMessage {

    /**
     * Execute the request client-side
     *
     * @param view The recipient component
     */
    void execute(View view);
}
