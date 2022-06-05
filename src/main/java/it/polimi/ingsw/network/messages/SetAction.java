package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.MessageType;

public class SetAction extends Message implements VCMessage {
	private final String nickname;
	private final Action action;


	public SetAction(String nickname, Action action){
		super(MessageType.VC);
		this.nickname = nickname;
		this.action = action;
	}

	@Override
	public void execute(GameController controller) {
		controller.setAction(action,nickname);
	}
}
