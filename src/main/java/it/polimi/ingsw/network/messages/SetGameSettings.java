package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class SetGameSettings implements VCMessage, Serializable {
	private final MessageType messageType;
	private final GameMode gameMode;
	private final int numPlayers;

	public SetGameSettings(GameMode gameMode,int numPlayers){
		this.messageType = MessageType.VC;
		this.gameMode = gameMode;
		this.numPlayers = numPlayers;
	}
	@Override
	public MessageType getType() {
		return messageType;
	}

	@Override
	public void execute(GameController controller) {
		controller.setNumPlayers(numPlayers);
		controller.setGameMode(gameMode);
	}
}
