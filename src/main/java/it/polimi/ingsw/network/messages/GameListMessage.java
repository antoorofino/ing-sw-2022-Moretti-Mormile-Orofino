package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;
import java.util.List;

public class GameListMessage implements Serializable, CVMessage {
    private final MessageType messageType;
    private final List<GameListInfo> gameList;

    public GameListMessage(List<GameListInfo> gameList){
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
