package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class SetNickname implements VCMessage, Serializable {
	private final MessageType messageType;
	private final String playerId;
	private final String nickname;


	public SetNickname(String playerId,String nickname){
		this.messageType = MessageType.VC;
		this.playerId = playerId;
		this.nickname = nickname;
	}

	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(GameController controller) {
		controller.setPlayerInfo(playerId,nickname);
	}
}
