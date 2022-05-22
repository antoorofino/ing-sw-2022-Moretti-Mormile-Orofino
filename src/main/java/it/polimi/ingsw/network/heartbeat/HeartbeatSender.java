package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.MessageType;

public class HeartbeatSender extends Thread{
    private final NetworkHandler networkHandler;
    private final boolean isServer;

    public HeartbeatSender(NetworkHandler networkHandler, boolean isServer) {
        this.isServer = isServer;
        this.networkHandler = networkHandler;
    }

    @Override
    public void run() {
        while(networkHandler.isConnected()){
            try {
                networkHandler.send(new HeartbeatMessage(isServer));
                Thread.sleep(Configurator.getHeartbeatInterval());
            } catch (InterruptedException ignored) {
            }
        }
    }
}