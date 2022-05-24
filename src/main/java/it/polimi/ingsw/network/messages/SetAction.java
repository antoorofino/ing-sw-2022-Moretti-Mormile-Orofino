package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.MessageType;
import it.polimi.ingsw.util.exception.PlayerException;

import java.io.Serializable;

public class SetAction implements VCMessage, Serializable {
	private final MessageType messageType;
	private final String nickname;
	private final Action action;


	public SetAction(String nickname, Action action){
		this.messageType = MessageType.VC;
		this.nickname = nickname;
		this.action = action;
	}

	@Override
	public MessageType getType() {
		return messageType;
	}


	@Override
	public void execute(GameController controller) {
		try {
			System.out.println("set action return value: " + controller.setAction(action,nickname));
		}catch(PlayerException e){
			System.out.println("Non è il tuo turno!");
		}
	}
}
