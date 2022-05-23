package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;
import it.polimi.ingsw.util.RoundActions;

import java.io.Serializable;

public class AskAction implements CVMessage, Serializable {
	private final MessageType messageType;
	private final RoundActions roundActions;
	private final boolean isInvalidAction;

	public AskAction(RoundActions roundActions,boolean isInvalidAction){
		this.messageType = MessageType.CV;
		this.roundActions = roundActions;
		this.isInvalidAction = isInvalidAction;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.askAction(roundActions,isInvalidAction);
	}
}
