package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class AskGameListMessage implements Serializable, SYSMessage {
    private final MessageType messageType;
    private final String playerId;

    public AskGameListMessage(String playerId){
        messageType = MessageType.SYS;
        this.playerId = playerId;
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void execute(ServerMain serverMain) {
        serverMain.sendActiveGames(playerId);
    }
}
