package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.MessageType;
import it.polimi.ingsw.util.TowerColor;

import java.io.Serializable;

public class SetTowerColor implements VCMessage, Serializable {
	private final MessageType messageType;
	private final String playerId;
	private final TowerColor color;


	public SetTowerColor(String playerId, TowerColor color){
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
