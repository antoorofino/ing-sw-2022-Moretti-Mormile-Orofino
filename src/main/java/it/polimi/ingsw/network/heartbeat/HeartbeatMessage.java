package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

import java.io.Serializable;

public class HeartbeatMessage extends Message implements CVMessage, SYSMessage {
    public HeartbeatMessage(boolean isServer) {
        super(isServer ? MessageType.CV : MessageType.SYS);
    }

    @Override
    public void execute(View view) {
    }

    @Override
    public void execute(ServerMain serverMain) {
    }
}

