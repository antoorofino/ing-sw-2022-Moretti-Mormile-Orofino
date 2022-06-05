package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

public class AskGameListMessage extends Message implements SYSMessage {
    private final String playerId;

    public AskGameListMessage(String playerId){
        super(MessageType.SYS);
        this.playerId = playerId;
    }

    @Override
    public void execute(ServerMain serverMain) {
        serverMain.sendActiveGames(playerId);
    }
}
