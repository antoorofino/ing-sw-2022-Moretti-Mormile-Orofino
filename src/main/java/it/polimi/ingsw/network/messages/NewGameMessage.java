package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.MessageType;

public class NewGameMessage extends Message implements SYSMessage {
    private final String playerId;
    private final GameListInfo gameInfo;

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
