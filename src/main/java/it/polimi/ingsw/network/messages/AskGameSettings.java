package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class AskGameSettings implements CVMessage, Serializable {
	private final MessageType messageType;

	public AskGameSettings(){
		this.messageType = MessageType.CV;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.askGameSettings();
	}

}