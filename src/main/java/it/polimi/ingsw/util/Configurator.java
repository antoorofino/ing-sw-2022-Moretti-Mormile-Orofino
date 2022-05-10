package it.polimi.ingsw.util;

public class Configurator {
    private static final int socketTimeout = 2000;

    public static int getSocketTimeout() {
        return Configurator.socketTimeout;
    }

    public static int getHeartbeatInterval(){
        return Configurator.socketTimeout/4;
    }
}
