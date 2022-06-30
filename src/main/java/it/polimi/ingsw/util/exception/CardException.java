package it.polimi.ingsw.util.exception;

/**
 * Exception thrown when the player plays an assistant card that he does not have
 */
public class CardException  extends Exception{
	public CardException(String errorMessage) {super(errorMessage);}
}
