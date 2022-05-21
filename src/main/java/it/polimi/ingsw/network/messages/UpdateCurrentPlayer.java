package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.GameInfo;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class UpdateCurrentPlayer implements CVMessage, Serializable {
	private final MessageType messageType;
	private final String currentPlayer;

	public UpdateCurrentPlayer(String currentPlayer){
		this.messageType = MessageType.CV;
		this.currentPlayer = currentPlayer;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.showTurn(currentPlayer);
	}
}
