package it.polimi.ingsw.util.exception;

/**
 * Exception thrown when it is not possible to trace the player by id or nickname
 */
public class PlayerException extends Exception{
    public PlayerException(String errorMessage) {super(errorMessage);}
}
