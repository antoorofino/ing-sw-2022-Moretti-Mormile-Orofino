package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.messages.AskNewGameChoice;
import it.polimi.ingsw.network.messages.AskNewGameName;
import it.polimi.ingsw.network.messages.AskNickname;
import it.polimi.ingsw.network.messages.GameListMessage;
import it.polimi.ingsw.util.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String HELP_ARGUMENT = "-h";
    private static final String PORT_ARGUMENT = "-p";
    private static final String VERBOSE_ARGUMENT = "-v";

    /**
     * Constructor: creates the server socket
     * @param portNumber listening port
     * @param verboseLevel level of logging
     * @throws IOException throws exception if there is a problem with socket creation
     */
    public ServerMain(int portNumber, int verboseLevel) throws IOException {
        serverSocket = new ServerSocket(portNumber);
        clientHandlerList = new ArrayList<>();
        gameControllerList = new ArrayList<>();
        logger = new Logger(verboseLevel);
    }

    /**
     * Starts the server
     * @param args arguments options to customize server behaviour
     */
    public static void main(String[] args) {
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        if (arguments.size() == 1 && arguments.contains(HELP_ARGUMENT)) {
            String helpMessage = "\nThis is the server for Eriantys game, with no arguments provided the server will start " +
                    "on port " + Configurator.getDefaultServerPort() + " with verbose log level " + Configurator.getDefaultVerboseLevel() + "\n\n" +
                    "Available argument options:\n" +
                    "-h: to get help\n" +
                    "-p: to specify a custom port number\n" +
                    "-v: to activate logging in the console. Possible levels of logging:\n" +
                    "   0 no log\n" +
                    "   1 only clients connections\n" +
                    "   2 basic clients communication\n" +
                    "   3 full clients communications and controller messages\n" +
                    "   4 in-game messages\n";
            System.out.println(helpMessage);
            return;
        }
        int portNumber;
        if (arguments.contains(PORT_ARGUMENT)) {
            try {
                if (InputValidator.isPortNumber(arguments.get(arguments.indexOf(PORT_ARGUMENT) + 1)))
                    portNumber = Integer.parseInt(arguments.get(arguments.indexOf(PORT_ARGUMENT) + 1));
                else
                    throw new Exception("Invalid port number");
            } catch (Exception e) {
                System.out.println("Invalid port number provided. Try again");
                return;
            }
            arguments.remove(PORT_ARGUMENT);
            arguments.remove(arguments.indexOf(PORT_ARGUMENT) + 1);
        } else {
            portNumber = Configurator.getDefaultServerPort();
        }
        int loggerLevelVerbose;
        if (arguments.contains(VERBOSE_ARGUMENT)) {
            try {
                String proposedLevel = arguments.get(arguments.indexOf(VERBOSE_ARGUMENT) + 1);
                if (InputValidator.isNumberBetween(Integer.parseInt(proposedLevel), 0, 4))
                    loggerLevelVerbose = Integer.parseInt(proposedLevel);
                else
                    throw new Exception("Invalid verbose level number");
            } catch (Exception e) {
                System.out.println("Invalid verbose log level provided. Try again");
                return;
            }
            arguments.remove(VERBOSE_ARGUMENT);
            arguments.remove(arguments.indexOf(VERBOSE_ARGUMENT) + 1);
        } else {
            loggerLevelVerbose = Configurator.getDefaultVerboseLevel();
        }
        if(arguments.size() > 0) {
            System.out.println("Invalid arguments provided. Use '-h' to find help");
            return;
        }
        // Creates and starts the server
        ServerMain serverMain;
        try {
            serverMain = new ServerMain(portNumber, loggerLevelVerbose);
        } catch (IOException e) {
            Logger.log('f', "Could not start the server. Check if the port is already used by other applications");
            return;
        }
        Logger.log('i', "Server listening on port " + portNumber);
        serverMain.listeningForClients();
    }

    /**
     * Listens for clients connections
     */
    public void listeningForClients() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, socket, logger);
                logger.log(1, 'i', "Client connected with id " + clientHandler.getPlayerId());
                synchronized (clientHandlerList) {
                    clientHandlerList.add(clientHandler);
                }
                clientHandler.start();
            } catch (IOException e) {
                Logger.log('f', "Could not connect the client");
            }
        }
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

    /**
     * Function to handle the creation of a new match by a client
     * @param playerId the id of the player
     * @param gameInfo contains the info about the game to create
     */
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

    /**
     * Function to handle when a client wants to enter an existing game
     * @param playerId the id of the player
     * @param gameName the name of the game chosen to join
     */
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

    /**
     * Helper function that retrieves a controller by the game name
     * @param gameName the name of the game
     * @return the controller that handles the game
     */
    private GameController getControllerByGameName(String gameName) {
        return gameControllerList.stream()
                .filter(c -> c.getGame().getGameName().equalsIgnoreCase(gameName))
                .findFirst().orElse(null);
    }

    /**
     * Helper function that retrieves a ClientHandler by the player id
     * @param playerId the id of the player
     * @return the player's ClientHandler
     */
    private ClientHandler getClientHandlerByPlayerId(String playerId){
        return clientHandlerList.stream()
                .filter( p -> Objects.equals(p.getPlayerId(), playerId))
                .findFirst().orElse(null);
    }

    /**
     * Sends the list of the games that accept players
     * @param playerId the id of the player to send the list to
     */
    public void sendActiveGames(String playerId){
        logger.log(2, 'i', "Player " + playerId + " has requested the list of active games");
        getClientHandlerByPlayerId(playerId).send(
                new GameListMessage(GameListInfo.createGameInfoList(gameModelList(true)))
        );
    }

    /**
     * Removes the ClientHandler of a player and the GameController of the game joined
     * @param playerId the id of the player
     * @param controllerToRemove GameController to remove
     */
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
     * Checks if there are no games with the that name
     * @param gameName the chosen game name
     * @return true if there are no games with the same name
     */
    private boolean checkName(String gameName){
        for (GameModel game : gameModelList(false)){
            if (game.getGameName().equalsIgnoreCase(gameName))
                return false;
        }
        return true;
    }
}
