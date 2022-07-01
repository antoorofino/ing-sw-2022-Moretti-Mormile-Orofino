package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.MessageType;

/**
 * This message is sent from the client to the server (VCMessage) when an action is performed by the
 * player
 */
public class SetAction extends Message implements VCMessage {
	private final String nickname;
	private final Action action;

	/**
	 * Constructor: builds the message
	 * @param nickname the nickname of the message’s sender
	 * @param action move of the player
	 */
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
