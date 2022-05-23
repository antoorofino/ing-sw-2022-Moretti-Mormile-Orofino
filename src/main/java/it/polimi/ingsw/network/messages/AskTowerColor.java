package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;
import java.util.ArrayList;

public class AskTowerColor implements CVMessage, Serializable {
	private final MessageType messageType;
	private final ArrayList<String> possibleColors;
	private final boolean isFirstRequest;

	public AskTowerColor(ArrayList<String> possibleColors,boolean isFirstRequest){
		this.messageType = MessageType.CV;
		this.possibleColors = possibleColors;
		this.isFirstRequest = isFirstRequest;
	}

	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.askTowerColor(possibleColors,isFirstRequest);
	}
}
