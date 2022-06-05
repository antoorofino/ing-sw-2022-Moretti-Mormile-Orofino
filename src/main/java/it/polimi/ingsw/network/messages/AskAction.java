package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;
import it.polimi.ingsw.util.RoundActions;

public class AskAction extends Message implements CVMessage {
	private final RoundActions roundActions;
	private final boolean isInvalidAction;

	public AskAction(RoundActions roundActions,boolean isInvalidAction){
		super(MessageType.CV);
		this.roundActions = roundActions;
		this.isInvalidAction = isInvalidAction;
	}

	@Override
	public void execute(View view) {
		view.askAction(roundActions,isInvalidAction);
	}
}
