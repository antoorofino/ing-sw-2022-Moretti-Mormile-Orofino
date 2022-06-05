package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.MessageType;

import java.util.List;

public class GameListMessage extends Message implements CVMessage {
    private final List<GameListInfo> gameList;

    public GameListMessage(List<GameListInfo> gameList){
        super(MessageType.CV);
        this.gameList = gameList;
    }

    @Override
    public void execute(View view) {
        view.showGamesList(gameList);
    }
}
