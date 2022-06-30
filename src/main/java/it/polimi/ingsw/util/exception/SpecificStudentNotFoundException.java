package it.polimi.ingsw.util.exception;

/**
 * Exception thrown when the required student is not found
 */
public class SpecificStudentNotFoundException extends Exception {
    public SpecificStudentNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
