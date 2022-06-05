package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

public class AskNickname extends Message implements CVMessage {
	private final boolean isFirstRequest;

	public AskNickname(boolean isFirstRequest){
		super(MessageType.CV);
		this.isFirstRequest = isFirstRequest;
	}

	@Override
	public void execute(View view) {
		view.askNickname(isFirstRequest);
	}
}
