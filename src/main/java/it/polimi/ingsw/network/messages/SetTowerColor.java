package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.util.MessageType;
import it.polimi.ingsw.util.TowerColor;

/**
 * VCMessage sent to the server with the chosen color
 */
public class SetTowerColor extends Message implements VCMessage {
	private final String playerId;
	private final TowerColor color;

	/**
	 * Constructor: builds the message
	 * @param playerId the ID of the message’s sender
	 * @param color the color chosen by the player
	 */
	public SetTowerColor(String playerId, TowerColor color){
		super(MessageType.VC);
		this.playerId = playerId;
		this.color = color;
	}

	@Override
	public void execute(GameController controller) {
		controller.setPlayerTowerColor(playerId, color);
	}
}
