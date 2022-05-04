package it.polimi.ingsw.util;

/**
 * Types of messages between client and server
 */
public enum MessageType {
    /**
     * Message from Controller to View
     */
    CV,

    /**
     * Message from View to Controller
     */
    VC,

    /**
     * System message between Server and Client
     */
    SYS
}