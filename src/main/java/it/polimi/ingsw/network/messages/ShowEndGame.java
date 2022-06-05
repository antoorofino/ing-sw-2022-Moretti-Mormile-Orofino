package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

public class ShowEndGame extends Message implements CVMessage {
	private final String winnerNickname;

	public ShowEndGame(String winnerNickname){
		super(MessageType.CV);
		this.winnerNickname = winnerNickname;
	}
	@Override
	public void execute(View view) {
		view.showGameEndMessage(winnerNickname);
	}
}
