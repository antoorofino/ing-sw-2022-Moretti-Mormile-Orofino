package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class SetTowerColor implements VCMessage, Serializable {
	private final MessageType messageType;
	private final String playerId;
	private final String color;


	public SetTowerColor(String playerId,String color){
		this.messageType = MessageType.VC;
		this.playerId = playerId;
		this.color = color;
	}

	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(GameController controller) {
		// TODO: add call to controller method
	}
}
