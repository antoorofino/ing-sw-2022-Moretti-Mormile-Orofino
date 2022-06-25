package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

/**
 * This message is sent from the server (CVMessage) to the client when the state of the game’s board
 * has been updated
 */
public class UpdateGameBoard extends Message implements CVMessage {
	private final GameModel game;

	/**
	 * Constructor: build the message
	 * @param game current state of the game board
	 */
	public UpdateGameBoard(GameModel game){
		super(MessageType.CV);
		this.game = game;
	}

	@Override
	public void execute(View view) {
		view.showGame(game);
	}
}
