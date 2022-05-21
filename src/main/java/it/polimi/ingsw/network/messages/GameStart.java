package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.GameInfo;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class GameStart implements CVMessage, Serializable {
	private final MessageType messageType;
	private final String firstPlayer;
	private final GameInfo gameInfo;

	public GameStart(GameInfo gameInfo, String firstPlayerNickname){
		this.messageType = MessageType.CV;
		this.gameInfo = gameInfo;
		this.firstPlayer = firstPlayerNickname;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(View view) {
		view.showGame(gameInfo);
		view.showTurn(firstPlayer);
	}
}
