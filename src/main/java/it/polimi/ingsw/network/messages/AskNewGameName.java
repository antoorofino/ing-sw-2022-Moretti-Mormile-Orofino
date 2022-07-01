package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

/**
 * This message is sent from the server to the client (CVMessage) to ask the player to provide a new
 * name of the game to create
 */
public class AskNewGameName extends Message implements CVMessage {

    /**
     * Constructor: builds the message
     */
    public AskNewGameName(){
        super(MessageType.CV);
    }

    @Override
    public void execute(View view) {
        view.askNewGameName();
    }
}
