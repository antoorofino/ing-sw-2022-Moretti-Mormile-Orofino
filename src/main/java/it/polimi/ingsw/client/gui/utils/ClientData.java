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

/**
 * Stores information about player logged in
 */
public class ClientData {
    private static ClientData instance = null;
    private String ipAddress;
    private int portNumber;
    private GameListInfo gameInfo;
    private GameModel game;
    private List<AssistantCard> possibleCards;
    private List<ActionType> possibleActions;

    /**
     * Constructor: Builds an instance of client data
     */
    private ClientData(){
        ipAddress = Configurator.getDefaultServerIp();
        portNumber = Configurator.getDefaultServerPort();
        possibleCards = null;
        resetPossibleActions();
    }

    /**
     * Creates a new instance of client data if not exist, returns the existing one
     * @return ClientData instance
     */
    public static ClientData getInstance() {
        if (instance == null)
            instance = new ClientData();
        return instance;
    }

    /**
     * Gets the ip of server
     * @return the ip of server
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Gets port number of server
     * @return port number of server
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * Sets ip address of server
     * @param ipAddress ip address of server
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Sets port number of server
     * @param portNumber port number of server
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * Gets the info about the game
     * @return info about the game
     */
    public GameListInfo getGameInfo() {
        return gameInfo;
    }

    /**
     * Sets info about the game
     * @param gameInfo info about the game
     */
    public void setGameInfo(GameListInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    /**
     * Gets current game
     * @return current game
     */
    public GameModel getGame() {
        return game;
    }

    /**
     * Stores current game
     * @param game current game
     */
    public void setGame(GameModel game) {
        this.game = game;
    }

    /**
     * Gets assistant cards that user can play
     * @return assistant cards that user can play
     */
    public List<AssistantCard> getPossibleCards() {
        return possibleCards;
    }

    /**
     * Stores assistant cards that user can play
     * @param possibleCards assistant cards that user can play
     */
    public void setPossibleCards(List<AssistantCard> possibleCards) {
        this.possibleCards = possibleCards;
    }

    /**
     * Gets player who is logged in
     * @return player who is logged in
     */
    public Player getPlayer() {
        try {
            return game.getPlayerHandler().getPlayerById(GUIView.getPlayerId());
        } catch (PlayerException e) {
            throw new RuntimeException("Player not found");
        }
    }

    /**
     * Gets the list of actions that player can do during the turn
     * @return list of possible action that player can du during the turn
     */
    public List<ActionType> getPossibleActions() {
        return new ArrayList<>(possibleActions);
    }

    /**
     * Stores all the actions that player can do during the turn
     * @param list of all the actions that player can do during the turn
     */
    public void setPossibleActions(RoundActions list) {
        possibleActions = list.getActionsList().stream().map(Action::getActionType).collect(Collectors.toList());
    }

    /**
     * Resets the list of actions that player can do during the turn
     */
    public void resetPossibleActions() {
        possibleActions = new ArrayList<>();
    }
}
