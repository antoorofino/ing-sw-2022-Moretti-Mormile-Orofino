package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.MessageType;

public class SetNickname extends Message implements VCMessage {
	private final String playerId;
	private final String nickname;

	public SetNickname(String playerId,String nickname){
		super(MessageType.VC);
		this.playerId = playerId;
		this.nickname = nickname;
	}

	@Override
	public void execute(GameController controller) {
		controller.setPlayerNickname(playerId, nickname);
	}
}
