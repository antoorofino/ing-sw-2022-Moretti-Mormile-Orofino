package it.polimi.ingsw.client.gui.utils;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.GameListInfo;

public class ClientData {
    private static ClientData instance = null;
    private String ipAddress;
    private int portNumber;
    private GameListInfo gameInfo;
    private GameModel game;
    private ClientData(){
        ipAddress = Configurator.getServerIp();
        portNumber = Configurator.getServerPort();
    }

    public static ClientData getInstance() {
        if (instance == null)
            instance = new ClientData();
        return instance;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public GameListInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameListInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public GameModel getGame() {
        return game;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }
}
