package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class ShowDisconnection implements CVMessage, Serializable {
	private final MessageType messageType;
	private final String disconnectedNickname;

	public ShowDisconnection(String disconnectedNickname){
		this.messageType = MessageType.CV;
		this.disconnectedNickname = disconnectedNickname;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.showErrorMessage("Il giocatore" + disconnectedNickname + " si è disconnesso");
	}
}
