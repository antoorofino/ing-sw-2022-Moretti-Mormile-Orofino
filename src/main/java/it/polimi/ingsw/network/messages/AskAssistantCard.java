package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

import java.util.List;

public class AskAssistantCard extends Message implements CVMessage {
	private final List<AssistantCard> possibleCards;
	private final GameModel game;

	public AskAssistantCard(List<AssistantCard> possibleCards, GameModel game){
		super(MessageType.CV);
		this.possibleCards = possibleCards;
		this.game = game;
	}

	@Override
	public void execute(View view) {
		view.askAssistantCard(possibleCards,game);
	}
}
