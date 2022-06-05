package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

public class AckTowerColor extends Message implements CVMessage {

    public AckTowerColor(){
        super(MessageType.CV);
    }

    @Override
    public void execute(View view) {
        view.showQueuedMessage();
    }
}
