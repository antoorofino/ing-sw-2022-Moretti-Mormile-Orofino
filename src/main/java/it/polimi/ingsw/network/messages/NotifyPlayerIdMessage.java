package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

public class NotifyPlayerIdMessage extends Message implements CVMessage {
    private final String playerId;

    public NotifyPlayerIdMessage(String playerId){
        super(MessageType.CV);
        this.playerId = playerId;
    }

    @Override
    public void execute(View view) {
        view.setPlayerId(playerId);
    }
}
