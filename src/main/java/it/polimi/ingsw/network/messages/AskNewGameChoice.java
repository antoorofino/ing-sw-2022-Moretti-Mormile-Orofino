package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;

public class AskNewGameChoice implements CVMessage {
    private final MessageType messageType;

    public AskNewGameChoice(){
        this.messageType = MessageType.CV;
    }

    @Override
    public void execute(View view) {
        view.askNewGameChoice();
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
