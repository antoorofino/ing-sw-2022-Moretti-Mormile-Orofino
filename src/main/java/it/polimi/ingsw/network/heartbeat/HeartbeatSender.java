package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.MessageType;

public class HeartbeatSender extends Thread{
    private final NetworkHandler networkHandler;
    private final MessageType messageType;

    public HeartbeatSender(NetworkHandler networkHandler,MessageType messageType) {
        this.messageType = messageType;
        this.networkHandler = networkHandler;
    }

    @Override
    public void run() {
        while(networkHandler.isConnected()){
            try {
                networkHandler.send(new HeartbeatMessage(messageType));
                Thread.sleep(Configurator.getHeartbeatInterval());
            } catch (InterruptedException ignored) {
            }
        }
    }
}