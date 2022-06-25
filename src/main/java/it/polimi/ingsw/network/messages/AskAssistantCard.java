package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

import java.util.List;

/**
 * This message is sent from the server to the client (CVMessage) to ask the player to choose an
 * assistant card
 */
public class AskAssistantCard extends Message implements CVMessage {
	private final List<AssistantCard> possibleCards;
	private final GameModel game;

	/**
	 * Constructor: build the message
	 * @param possibleCards list of possible cards that can be chosen by the play
	 * @param game the current state of the game board
	 */
	public AskAssistantCard(List<AssistantCard> possibleCards, GameModel game){
		super(MessageType.CV);
		this.possibleCards = possibleCards;
		this.game = game;
	}

	@Override
	public void execute(View view) {
		view.askAssistantCard(possibleCards,game);
	}
}
