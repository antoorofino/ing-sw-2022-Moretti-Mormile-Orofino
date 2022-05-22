package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.GamesListInfo;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class GameListMessage implements Serializable, CVMessage {
    private final MessageType messageType;
    private final GamesListInfo gameList;

    public GameListMessage(GamesListInfo gameList){
        this.gameList = gameList;
        this.messageType = MessageType.CV;
    }
    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void execute(View view) {
        view.showGamesList(gameList);
    }
}
