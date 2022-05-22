package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class HeartbeatMessage implements CVMessage, SYSMessage, Serializable {
    private final MessageType messageType;
    public HeartbeatMessage(boolean isServer) {
        if (isServer)
            this.messageType = MessageType.CV;
        else
            this.messageType = MessageType.SYS;
    }

    @Override
    public MessageType getType() {
        return messageType;
    }


    @Override
    public void execute(View view) {
    }

    @Override
    public void execute(ServerMain serverMain) {
    }
}

