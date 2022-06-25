package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

/**
 * Message sent to the server with the chosen game’s name
 */
public class SelectGameMessage extends Message implements SYSMessage {
    private final String gameName;
    private final String playerId;

    /**
     * Constructor: build the message
     * @param playerId the ID of the message’s sender
     * @param gameName the name of the game you want to join
     */
    public SelectGameMessage(String playerId, String gameName){
        super(MessageType.SYS);
        this.playerId = playerId;
        this.gameName = gameName;
    }

    @Override
    public void execute(ServerMain serverMain) {
        serverMain.selectGame(playerId, gameName);
    }
}
