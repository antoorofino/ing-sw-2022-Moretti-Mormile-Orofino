package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class GameStart implements CVMessage, Serializable {
	private final MessageType messageType;
	private final String firstPlayer;
	private final GameModel game;

	public GameStart(GameModel game, String firstPlayerNickname){
		this.messageType = MessageType.CV;
		this.game = game;
		this.firstPlayer = firstPlayerNickname;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.showGame(game);
	}
}
