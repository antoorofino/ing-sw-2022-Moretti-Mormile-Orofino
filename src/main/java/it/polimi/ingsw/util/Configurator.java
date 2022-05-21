package it.polimi.ingsw.util;

public class Configurator {
    private static final int socketTimeout = 2000;
    private static final int serverPort = 8090;

    public static int getSocketTimeout() {
        return Configurator.socketTimeout;
    }

    public static int getHeartbeatInterval(){
        return Configurator.socketTimeout/4;
    }

    public static int getServerPort(){
        return Configurator.serverPort;
    }
}
