package it.polimi.ingsw.network;

import it.polimi.ingsw.util.MessageType;

/**
 * Message between client and server
 */
public interface Message {

    /**
     * Gets the message type
     *
     * @return The message type
     */
    MessageType getType();
}
