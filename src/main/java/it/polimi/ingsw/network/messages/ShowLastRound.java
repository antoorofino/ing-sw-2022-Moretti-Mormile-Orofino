package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

/**
 * This message is sent from the server to each client (CVMessage) when either a player ends his cards
 * or the students’ bag is empty. The game will finish at the end of the current round
 */
public class ShowLastRound extends Message implements CVMessage {

	/**
	 * Constructor: builds the message
	 */
	public ShowLastRound(){
		super(MessageType.CV);
	}

	@Override
	public void execute(View view) {
		view.showLastRound();
	}
}
