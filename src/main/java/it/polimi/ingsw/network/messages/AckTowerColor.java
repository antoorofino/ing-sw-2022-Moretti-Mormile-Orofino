package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class AckTowerColor implements Serializable, CVMessage {
    private final MessageType messageType;

    public AckTowerColor(){
        this.messageType = MessageType.CV;
    }

    @Override
    public void execute(View view) {
        view.showQueuedMessage();
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}