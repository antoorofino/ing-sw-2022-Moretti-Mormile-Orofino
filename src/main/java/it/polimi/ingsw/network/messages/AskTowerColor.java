package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;
import it.polimi.ingsw.util.TowerColor;

import java.util.List;

/**
 * This message is sent from the server to the client (CVMessage) to ask the player to choose the color
 * of the towers
 */
public class AskTowerColor extends Message implements CVMessage {
	private final List<TowerColor> possibleColors;
	private final boolean isFirstRequest;

	/**
	 * Constructor: builds the message
	 * @param possibleColors the available colors
	 * @param isFirstRequest true if it is the first request
	 */
	public AskTowerColor(List<TowerColor> possibleColors,boolean isFirstRequest){
		super(MessageType.CV);
		this.possibleColors = possibleColors;
		this.isFirstRequest = isFirstRequest;
	}

	@Override
	public void execute(View view) {
		view.askTowerColor(possibleColors,isFirstRequest);
	}
}
