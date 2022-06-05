package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.network.messages.AskNewGameChoice;
import it.polimi.ingsw.network.messages.AskNewGameName;
import it.polimi.ingsw.network.messages.AskNickname;
import it.polimi.ingsw.network.messages.GameListMessage;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameStatus;
import it.polimi.ingsw.util.exception.DisconnectionException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServerMain {
    private final ServerSocket serverSocket;
    private final List<ClientHandler> clientHandlerList;
    private final List<GameController> gameControllerList;

    public ServerMain() throws IOException {
        serverSocket = new ServerSocket(Configurator.getServerPort());
        clientHandlerList = new ArrayList<>();
        gameControllerList = new ArrayList<>();
    }

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
            System.out.println("FATAL ERROR: Could not start the server");
            return;
        }
        try {
            System.out.println("INFO: Server listening on port " + Configurator.getServerPort());
            serverMain.listeningForClients();
        } catch (IOException e) {
            System.out.println("FATAL ERROR: ould not connect the client");
        }
    }

    public void listeningForClients() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(this, socket);
            System.out.println("INFO: Client connected with id " + clientHandler.getPlayerId());
            synchronized (clientHandlerList) {
                clientHandlerList.add(clientHandler);
            }
            clientHandler.start();
        }
    }

    public void createNewGame(String playerId, GameListInfo gameInfo){
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        System.out.println("INFO: Player " + playerId + " has requested to create game " + gameInfo.getGameName());
        synchronized (gameControllerList) {
            if(checkName(gameInfo.getGameName())){
                GameController controller = new GameController(gameInfo);
                controller.addClientHandler(clientHandler);
                controller.addPlayer(playerId);
                System.out.println("INFO: Player " + playerId + " added to the game " + gameInfo.getGameName());
                gameControllerList.add(controller);
                clientHandler.setController(controller);
                (new Thread(() -> {
                    try {
                        controller.gameRunner();
                    } catch (Exception ignored) {
                    }
                })).start();
                System.out.println("INFO: Game " + gameInfo.getGameName() + " created and controller started");
                clientHandler.send(new AskNickname(true));
            } else {
                System.out.println("ERROR: name " + gameInfo.getGameName() + " is already used");
                clientHandler.send(new AskNewGameName());
            }
        }
    }

    public void selectGame(String playerId, String gameName){
        System.out.println("INFO: Player " + playerId + " has requested to join game " + gameName);
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        synchronized (gameControllerList) {
            GameController controller = getControllerByGameName(gameName);
            if(controller == null || controller.getStatus() != GameStatus.ACCEPT_PLAYERS) {
                System.out.println("ERROR: Impossible to join game " + gameName);
                clientHandler.send(new AskNewGameChoice());
            } else {
                controller.addClientHandler(clientHandler);
                controller.addPlayer(playerId);
                System.out.println("INFO: Player " + playerId + " added to the game " + gameName);
                clientHandler.setController(controller);
                if (controller.getStatus() != GameStatus.ACCEPT_PLAYERS){
                    gameControllerList.remove(controller);
                    //TODO: verbose
                    System.out.println("INFO-VERBOSE: Game controller of game " + gameName + " detached from ServerMain");
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
        System.out.println("INFO: Player " + playerId + " has requested the list of active games");
        getClientHandlerByPlayerId(playerId).send(
                new GameListMessage(GameListInfo.createGameInfoList(gameModelList(true)))
        );
    }

    public void removeClientHandlerById(String playerId, GameController controllerToRemove){
        synchronized (gameControllerList) {
            try {
                gameControllerList.remove(controllerToRemove);
                //TODO: verbose
                System.out.println("INFO-VERBOSE: GameController of game " +
                        controllerToRemove.getGame().getGameName() +
                        " removed from clientHandlerList");
            } catch (Exception ignored) {

            }
        }
        synchronized (clientHandlerList) {
            try {
                clientHandlerList.remove(getClientHandlerByPlayerId(playerId));
                //TODO: verbose
                System.out.println("INFO-VERBOSE: ClientHandler " +
                        playerId + " removed from clientHandlerList");
            } catch (Exception ignored){
            }
        }
    }

    private boolean checkName(String gameName){
        for (GameModel game : gameModelList(false)){
            if (game.getGameName().equalsIgnoreCase(gameName))
                return false;
        }
        return true;
    }
}
