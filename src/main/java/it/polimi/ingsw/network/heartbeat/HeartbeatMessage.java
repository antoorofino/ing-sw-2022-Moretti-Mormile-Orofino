package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.util.MessageType;

/**
 * Message exchanged between server and client to check connection
 */
public class HeartbeatMessage extends Message implements CVMessage, SYSMessage {

    /**
     * Constructor: builds the Heartbeat message
     * @param isServer true if it is sent by the server
     */
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

