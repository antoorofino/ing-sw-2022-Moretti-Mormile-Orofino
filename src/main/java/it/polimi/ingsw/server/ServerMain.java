package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.messages.AskNewGameChoice;
import it.polimi.ingsw.network.messages.AskNewGameName;
import it.polimi.ingsw.network.messages.AskNickname;
import it.polimi.ingsw.network.messages.GameListMessage;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameStatus;
import it.polimi.ingsw.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Initializes the server and accepts client connections
 */
public class ServerMain {
    private final ServerSocket serverSocket;
    private final List<ClientHandler> clientHandlerList;
    private final List<GameController> gameControllerList;
    private final Logger logger;

    /**
     * Constructor: creates the server socket
     * @throws IOException throws exception if there is a problem with socket creation
     */
    public ServerMain() throws IOException {
        serverSocket = new ServerSocket(Configurator.getServerPort());
        clientHandlerList = new ArrayList<>();
        gameControllerList = new ArrayList<>();
        logger = new Logger(4);
    }

    /**
     * Returns the list of available games model
     * @param onlyAcceptPlayers true if he only wants games that accept players
     * @return the list of available games
     */
    private List<GameModel> gameModelList(Boolean onlyAcceptPlayers){
        Predicate<GameController> filter;
        if (onlyAcceptPlayers)
            filter = c -> c.getStatus() == GameStatus.ACCEPT_PLAYERS;
        else
            filter = c -> true;
        return gameControllerList.stream()
                .filter(filter)
                .map(GameController::getGame)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        ServerMain serverMain;
        try {
            serverMain = new ServerMain();
        } catch (IOException e) {
            Logger.log('f', "Could not start the server");
            return;
        }
        try {
            Logger.log('i', "Server listening on port " + Configurator.getServerPort());
            serverMain.listeningForClients();
        } catch (IOException e) {
            Logger.log('f', "Could not connect the client");
        }
    }

    public void listeningForClients() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(this, socket, logger);
            logger.log(1, 'i', "Client connected with id " + clientHandler.getPlayerId());
            synchronized (clientHandlerList) {
                clientHandlerList.add(clientHandler);
            }
            clientHandler.start();
        }
    }

    public void createNewGame(String playerId, GameListInfo gameInfo){
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        logger.log(2, 'i', "Player " + playerId + " has requested to create game " + gameInfo.getGameName());
        synchronized (gameControllerList) {
            if(checkName(gameInfo.getGameName())){
                GameController controller = new GameController(gameInfo, logger);
                controller.addClientHandler(clientHandler);
                controller.addPlayer(playerId);
                logger.log(2, 'i', "Player " + playerId + " added to the game " + gameInfo.getGameName());
                gameControllerList.add(controller);
                clientHandler.setController(controller);
                (new Thread(() -> {
                    try {
                        controller.gameRunner();
                    } catch (Exception ignored) {
                    }
                })).start();
                logger.log(3, 'i', "Game " + gameInfo.getGameName() + " created and controller started");
                clientHandler.send(new AskNickname(true));
            } else {
                logger.log(2, 'w', "Name " + gameInfo.getGameName() + " is already used");
                clientHandler.send(new AskNewGameName());
            }
        }
    }

    public void selectGame(String playerId, String gameName){
        logger.log(2, 'i', "Player " + playerId + " has requested to join game " + gameName);
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        synchronized (gameControllerList) {
            GameController controller = getControllerByGameName(gameName);
            if(controller == null || controller.getStatus() != GameStatus.ACCEPT_PLAYERS) {
                logger.log(2, 'w', "Impossible to join game " + gameName);
                clientHandler.send(new AskNewGameChoice());
            } else {
                controller.addClientHandler(clientHandler);
                controller.addPlayer(playerId);
                logger.log(2, 'i', "Player " + playerId + " added to the game " + gameName);
                clientHandler.setController(controller);
                if (controller.getStatus() != GameStatus.ACCEPT_PLAYERS){
                    gameControllerList.remove(controller);
                    logger.log(3, 'i', "Game controller of game " + gameName + " detached from ServerMain");
                }
                clientHandler.send(new AskNickname(true));
            }
        }
    }

    private GameController getControllerByGameName(String gameName) {
        return gameControllerList.stream()
                .filter(c -> c.getGame().getGameName().equalsIgnoreCase(gameName))
                .findFirst().orElse(null);
    }

    private ClientHandler getClientHandlerByPlayerId(String playerId){
        return clientHandlerList.stream()
                .filter( p -> Objects.equals(p.getPlayerId(), playerId))
                .findFirst().orElse(null);
    }

    public void sendActiveGames(String playerId){
        logger.log(2, 'i', "Player " + playerId + " has requested the list of active games");
        getClientHandlerByPlayerId(playerId).send(
                new GameListMessage(GameListInfo.createGameInfoList(gameModelList(true)))
        );
    }

    public void removeClientHandlerById(String playerId, GameController controllerToRemove){
        synchronized (gameControllerList) {
            try {
                gameControllerList.remove(controllerToRemove);
                logger.log(3, 'i',"GameController of game " +
                        controllerToRemove.getGame().getGameName() +
                                " removed from clientHandlerList");
            } catch (Exception ignored) {
            }
        }
        synchronized (clientHandlerList) {
            try {
                clientHandlerList.remove(getClientHandlerByPlayerId(playerId));
                logger.log(3, 'i', "ClientHandler " +
                        playerId + " removed from clientHandlerList" );
            } catch (Exception ignored){
            }
        }
    }

    /**
     * Checks if there are no games with that name
     * @param gameName the chosen game's name
     * @return true if there are no games with that name
     */
    private boolean checkName(String gameName){
        for (GameModel game : gameModelList(false)){
            if (game.getGameName().equalsIgnoreCase(gameName))
                return false;
        }
        return true;
    }
}
