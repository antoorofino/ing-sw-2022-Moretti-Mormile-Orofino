package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

public class UpdateGameBoard extends Message implements CVMessage {
	private final GameModel game;

	public UpdateGameBoard(GameModel game){
		super(MessageType.CV);
		this.game = game;
	}

	@Override
	public void execute(View view) {
		view.showGame(game);
	}
}
