package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class ShowDisconnectionMessage implements Serializable, CVMessage {
    private final MessageType messageType;
    private final String disconnectedNickname;

    public ShowDisconnectionMessage(String playerNickname){
        messageType = MessageType.CV;
        disconnectedNickname = playerNickname;
    }

    @Override
    public void execute(View view) {
        view.showErrorMessage("Il giocatore " + disconnectedNickname + " si è disconnesso");
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
