package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.MessageType;

/**
 * This message, which belongs to the SYSMessages class, is sent by the player to provide the new
 * game configuration
 */
public class NewGameMessage extends Message implements SYSMessage {
    private final String playerId;
    private final GameListInfo gameInfo;

    /**
     * Constructor: build the message
     * @param playerId the ID of the message’s sender
     * @param gameInfo contains the new game name, the number of players (2..3) and the game mode
     * (Basic, Expert)
     */
    public NewGameMessage(String playerId, GameListInfo gameInfo){
        super(MessageType.SYS);
        this.playerId = playerId;
        this.gameInfo = gameInfo;
    }

    @Override
    public void execute(ServerMain serverMain) {
        serverMain.createNewGame(playerId, gameInfo);
    }
}
