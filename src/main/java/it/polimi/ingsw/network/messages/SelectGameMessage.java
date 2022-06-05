package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

public class SelectGameMessage extends Message implements SYSMessage {
    private final String gameName;
    private final String playerId;

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
