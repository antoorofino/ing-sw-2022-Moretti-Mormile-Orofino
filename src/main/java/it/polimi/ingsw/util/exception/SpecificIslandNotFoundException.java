package it.polimi.ingsw.util.exception;

/**
 * Exception thrown when the required island is not found
 */
public class SpecificIslandNotFoundException extends Exception{
    public SpecificIslandNotFoundException(String errorMessage) {super(errorMessage);}
}
