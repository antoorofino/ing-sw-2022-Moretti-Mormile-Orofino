package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class SetAssistantCard implements VCMessage, Serializable {
	private final MessageType messageType;
	private final String playerId;
	private final AssistantCard assistantCard;


	public SetAssistantCard(String playerId, AssistantCard assistantCard){
		// FIXME: playerId or nickname?
		this.messageType = MessageType.VC;
		this.playerId = playerId;
		this.assistantCard = assistantCard;
	}

	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(GameController controller) {
		controller.setAssistantCard(playerId,assistantCard);
	}
}
