package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.GameInfo;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class UpdateGameBoard implements CVMessage, Serializable {
	private final MessageType messageType;
	private final GameInfo gameInfo;

	public UpdateGameBoard(GameInfo gameInfo){
		this.messageType = MessageType.CV;
		this.gameInfo = gameInfo;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.showGame(gameInfo);
	}
}
