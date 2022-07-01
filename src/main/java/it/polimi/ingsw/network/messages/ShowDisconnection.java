package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

/**
 * This message is sent from the server to every client, except the disconnected one, to
 * report that the game must end due to the disconnection of a player
 */
public class ShowDisconnection extends Message implements CVMessage {
    private final String disconnectedNickname;

    /**
     * Constructor: builds the message
     * @param playerNickname nickname of the player who disconnected from the match
     */
    public ShowDisconnection(String playerNickname){
        super(MessageType.CV);
        disconnectedNickname = playerNickname;
    }

    @Override
    public void execute(View view) {
        view.showDisconnection(disconnectedNickname);
    }
}
