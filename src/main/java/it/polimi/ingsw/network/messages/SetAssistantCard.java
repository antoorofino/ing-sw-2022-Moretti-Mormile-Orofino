package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.MessageType;

/**
 * VCMessage sent to the server to set the assistant card
 */
public class SetAssistantCard extends Message implements VCMessage {
	private final String nickname;
	private final AssistantCard assistantCard;

	/**
	 * Constructor: builds the message
	 * @param nickname the nickname of the message’s sender
	 * @param assistantCard the card chosen by the player
	 */
	public SetAssistantCard(String nickname, AssistantCard assistantCard){
		super(MessageType.VC);
		this.nickname = nickname;
		this.assistantCard = assistantCard;
	}

	@Override
	public void execute(GameController controller) {
		controller.setAssistantCard(nickname,assistantCard);
	}
}
