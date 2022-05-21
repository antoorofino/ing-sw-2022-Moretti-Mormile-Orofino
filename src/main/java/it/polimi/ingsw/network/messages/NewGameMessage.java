package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class NewGameMessage implements Serializable, SYSMessage {
    private final MessageType messageType;
    private final String playerId;

    public NewGameMessage(String playerId){
        this.messageType = MessageType.SYS;
        this.playerId = playerId;
    }
    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void execute(ServerMain serverMain) {
        serverMain.createNewGame(playerId);
    }
}
