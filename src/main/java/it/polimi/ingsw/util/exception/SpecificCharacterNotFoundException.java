package it.polimi.ingsw.util.exception;

/**
 * Exception thrown when the required character card is not found
 */
public class SpecificCharacterNotFoundException extends Exception{
    public SpecificCharacterNotFoundException(String errorMessage) {super(errorMessage);}
}
