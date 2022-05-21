package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class ShowEndGame implements CVMessage, Serializable {
	private final MessageType messageType;
	private final String winnerNickname;

	public ShowEndGame(String winnerNickname){
		this.messageType = MessageType.CV;
		this.winnerNickname = winnerNickname;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.showGameEndMessage(winnerNickname);
	}
}
