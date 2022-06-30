package it.polimi.ingsw.util.exception;

/**
 * Exception thrown when the required cloud is not found
 */
public class SpecificCloudNotFoundException extends Exception{
    public SpecificCloudNotFoundException(String errorMessage) {super(errorMessage);}
}
