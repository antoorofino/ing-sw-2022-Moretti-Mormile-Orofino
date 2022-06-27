package it.polimi.ingsw.client.gui.utils;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.*;
import it.polimi.ingsw.util.exception.PlayerException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientData {
    private static ClientData instance = null;
    private String ipAddress;
    private int portNumber;
    private GameListInfo gameInfo;
    private GameModel game;
    private List<AssistantCard> possibleCards;
    private List<ActionType> possibleActions;
    private ClientData(){
        ipAddress = Configurator.getServerIp();
        portNumber = Configurator.getServerPort();
        possibleCards = null;
        resetPossibleActions();
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

    public List<AssistantCard> getPossibleCards() {
        return possibleCards;
    }

    public void setPossibleCards(List<AssistantCard> possibleCards) {
        this.possibleCards = possibleCards;
    }

    public Player getPlayer() {
        try {
            return game.getPlayerHandler().getPlayerById(GUIView.getPlayerId());
        } catch (PlayerException e) {
            throw new RuntimeException("Player not found");
        }
    }

    public List<ActionType> getPossibleActions() {
        return new ArrayList<>(possibleActions);
    }

    public void setPossibleActions(RoundActions list) {
        possibleActions = list.getActionsList().stream().map(Action::getActionType).collect(Collectors.toList());
    }

    public void resetPossibleActions() {
        possibleActions = new ArrayList<>();
    }
}
