package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.util.Configurator;

public class HeartbeatSender extends Thread{
    private final NetworkHandler networkHandler;

    public HeartbeatSender(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    @Override
    public void run() {
        while(networkHandler.isConnected()){
            try {
                Thread.sleep(Configurator.getHeartbeatInterval());
            } catch (InterruptedException ignored) {
            }
        }
    }
}