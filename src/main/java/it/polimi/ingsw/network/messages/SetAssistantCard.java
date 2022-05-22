package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class SetAssistantCard implements VCMessage, Serializable {
	private final MessageType messageType;
	private final String nickname;
	private final AssistantCard assistantCard;


	public SetAssistantCard(String nickname, AssistantCard assistantCard){
		this.messageType = MessageType.VC;
		this.nickname = nickname;
		this.assistantCard = assistantCard;
	}

	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(GameController controller) {
		controller.setAssistantCard(nickname,assistantCard);
	}
}
