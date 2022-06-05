package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;
import it.polimi.ingsw.util.TowerColor;

import java.util.List;

public class AskTowerColor extends Message implements CVMessage {
	private final List<TowerColor> possibleColors;
	private final boolean isFirstRequest;

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
