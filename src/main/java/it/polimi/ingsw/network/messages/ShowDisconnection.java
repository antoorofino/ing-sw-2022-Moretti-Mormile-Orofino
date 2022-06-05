package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.util.MessageType;

public class ShowDisconnection extends Message implements CVMessage {
    private final String disconnectedNickname;

    public ShowDisconnection(String playerNickname){
        super(MessageType.CV);
        disconnectedNickname = playerNickname;
    }

    @Override
    public void execute(View view) {
        view.showDisconnection(disconnectedNickname);
    }
}
