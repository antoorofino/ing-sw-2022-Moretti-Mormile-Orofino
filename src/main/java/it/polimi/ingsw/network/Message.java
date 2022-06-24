package it.polimi.ingsw.network;

import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

/**
 * Message between client and server
 */
public abstract class Message implements Serializable {
    private final MessageType messageType;

    /**
     * Constructor: build the message
     * @param messageType identifies the direction of the message
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Gets the message type
     * @return The message type
     */
    public MessageType getType() {
        return messageType;
    }
}
