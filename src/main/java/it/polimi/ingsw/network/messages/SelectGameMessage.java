package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class SelectGameMessage implements Serializable, SYSMessage {
    private final MessageType messageType;
    private final GameModel game;
    private final String playerId;

    public SelectGameMessage(String playerId, GameModel game){
        this.messageType = MessageType.SYS;
        this.playerId = playerId;
        this.game = game;
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void execute(ServerMain serverMain) {
        serverMain.selectGame(playerId, game);
    }
}
