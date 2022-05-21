package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.util.MessageType;

public class HeartbeatMessage implements CVMessage, VCMessage, Message {
    private final MessageType messageType = MessageType.SYS;

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void execute(View view) {
    }

    @Override
    public void execute(VirtualView virtualView) {
    }
}

