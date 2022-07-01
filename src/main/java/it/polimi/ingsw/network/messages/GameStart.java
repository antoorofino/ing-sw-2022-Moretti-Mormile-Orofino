package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

/**
 * This message is sent to every client to inform that the game is ready to start
 * after two or three players have been matched. Moreover it specifies the first player chosen randomly
 */
public class GameStart extends Message implements CVMessage {
	private final String firstPlayer;
	private final GameModel game;

	/**
	 * Constructor: builds the message
	 * @param game the current state of the game board
	 * @param firstPlayerNickname the nickname of the first player
	 */
	public GameStart(GameModel game, String firstPlayerNickname){
		super(MessageType.CV);
		this.game = game;
		this.firstPlayer = firstPlayerNickname;
	}

	@Override
	public void execute(View view) {
		view.showGameStart(game, firstPlayer);
	}
}
