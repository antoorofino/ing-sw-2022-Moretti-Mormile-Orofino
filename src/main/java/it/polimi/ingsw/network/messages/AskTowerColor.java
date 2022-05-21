package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;
import java.util.ArrayList;

public class AskTowerColor implements CVMessage, Serializable {
	private final MessageType messageType;
	private final ArrayList<String> possibleColors;

	public AskTowerColor(ArrayList<String> possibleColors){
		this.messageType = MessageType.CV;
		this.possibleColors = possibleColors;
	}

	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.askTowerColor(possibleColors);
	}
}
