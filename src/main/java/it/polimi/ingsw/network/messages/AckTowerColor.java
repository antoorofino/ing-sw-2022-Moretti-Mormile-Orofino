package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

/**
 * This message is sent from the server to the client to ask the player to choose the color
 * of the towers
 */
public class AckTowerColor extends Message implements CVMessage {

    /**
     * Constructor: builds the message
     */
    public AckTowerColor(){
        super(MessageType.CV);
    }

    @Override
    public void execute(View view) {
        view.showQueuedMessage();
    }
}
