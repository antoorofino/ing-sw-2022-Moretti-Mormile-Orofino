package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.util.Configurator;

/**
 * Creates threads to exchange ping messages between server and client
 */
public class HeartbeatSender extends Thread{
    private final NetworkHandler networkHandler;
    private final boolean isServer;

    /**
     * Constructor: builds the sender
     * @param networkHandler clientHandler or serverHandler
     * @param isServer true if is server side
     */
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