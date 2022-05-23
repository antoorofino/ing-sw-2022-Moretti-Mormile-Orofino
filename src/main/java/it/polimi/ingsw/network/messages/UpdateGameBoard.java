package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class UpdateGameBoard implements CVMessage, Serializable {
	private final MessageType messageType;
	private final GameModel game;

	public UpdateGameBoard(GameModel game){
		this.messageType = MessageType.CV;
		this.game = game;
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
