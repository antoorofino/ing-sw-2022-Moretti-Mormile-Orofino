package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

/**
 * CVMessage sent to each client to report the end of the game
 */
public class ShowEndGame extends Message implements CVMessage {
	private final String winnerNickname;

	/**
	 * Constructor: build the message
	 * @param winnerNickname nickname of the winner or null if no winner could be determined
	 */
	public ShowEndGame(String winnerNickname){
		super(MessageType.CV);
		this.winnerNickname = winnerNickname;
	}

	@Override
	public void execute(View view) {
		view.showGameEndMessage(winnerNickname);
	}
}
