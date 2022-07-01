package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

/**
 * This message, which belongs to the SYSMessages class, is sent by the client to ask for the list of
 * available games
 */
public class AskGameListMessage extends Message implements SYSMessage {
    private final String playerId;

    /**
     * Constructor: builds the message
     * @param playerId the ID of the message’s sender
     */
    public AskGameListMessage(String playerId){
        super(MessageType.SYS);
        this.playerId = playerId;
    }

    @Override
    public void execute(ServerMain serverMain) {
        serverMain.sendActiveGames(playerId);
    }
}
