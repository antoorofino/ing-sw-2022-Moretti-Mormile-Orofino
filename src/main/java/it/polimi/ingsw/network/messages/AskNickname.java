package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

/**
 * This message is sent from the server to the client (CVMessage) to ask the player to provide a nickname
 */
public class AskNickname extends Message implements CVMessage {
	private final boolean isFirstRequest;

	/**
	 * Constructor: builds the message
	 * @param isFirstRequest true if it is the first request.
	 */
	public AskNickname(boolean isFirstRequest){
		super(MessageType.CV);
		this.isFirstRequest = isFirstRequest;
	}

	@Override
	public void execute(View view) {
		view.askNickname(isFirstRequest);
	}
}
