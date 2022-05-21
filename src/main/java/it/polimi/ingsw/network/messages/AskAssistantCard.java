package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;
import java.util.ArrayList;

public class AskAssistantCard implements CVMessage, Serializable {
	private final MessageType messageType;
	private final ArrayList<AssistantCard> possibleCards;

	public AskAssistantCard(ArrayList<AssistantCard> possibleCards){ // TODO: Assistant Card should be Serializable
		this.messageType = MessageType.CV;
		this.possibleCards = possibleCards;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.askAssistantCard(possibleCards);
	}
}
