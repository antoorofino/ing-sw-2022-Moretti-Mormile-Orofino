package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.util.MessageType;
import org.apache.logging.log4j.message.Message;

public class AskNewGameName implements CVMessage {
    private final MessageType messageType;

    public AskNewGameName(){
        this.messageType = MessageType.CV;
    }

    @Override
    public void execute(View view) {
        //TODO: add view method
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
