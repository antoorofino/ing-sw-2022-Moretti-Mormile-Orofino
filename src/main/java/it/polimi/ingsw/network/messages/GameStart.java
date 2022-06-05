package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

public class GameStart extends Message implements CVMessage {
	private final String firstPlayer;
	private final GameModel game;

	public GameStart(GameModel game, String firstPlayerNickname){
		super(MessageType.CV);
		this.game = game;
		this.firstPlayer = firstPlayerNickname;
	}

	@Override
	public void execute(View view) {
		view.showGameStart(game, firstPlayer);
	}
}
