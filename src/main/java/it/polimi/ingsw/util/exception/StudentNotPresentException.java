package it.polimi.ingsw.util.exception;

/**
 * Exception thrown when no student is found
 */
public class StudentNotPresentException extends Exception {
	public StudentNotPresentException(String errorMessage) {
		super(errorMessage);
	}
}
