package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class NotifyPlayerIdMessage implements Serializable, CVMessage {
    private final MessageType messageType;
    private final String playerId;

    public NotifyPlayerIdMessage(String playerId){
        this.messageType = MessageType.CV;
        this.playerId = playerId;
    }

    @Override
    public void execute(View view) {
        view.setPlayerId(playerId);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
