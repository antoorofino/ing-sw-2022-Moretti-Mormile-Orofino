package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.AskNewGameName;
import it.polimi.ingsw.network.messages.AskNickname;
import it.polimi.ingsw.network.messages.GameListMessage;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.GameListInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private List<GameModel> gameModelList(){
        return gameControllerList.stream()
                .map(GameController::getGame)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        ServerMain serverMain;
        try {
            serverMain = new ServerMain();
        } catch (IOException e) {
            System.out.println("Fatal error: Could not start the server.");
            return;
        }
        try {
            System.out.println("Server listening on port " + Configurator.getServerPort());
            serverMain.launch();
        } catch (IOException e) {
            System.out.println("Fatal error: Could not connect the client");
        }
    }

    public void launch() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(this, socket);
            System.out.println("Player connected with id " + clientHandler.getPlayerId());
            synchronized (clientHandlerList) {
                clientHandlerList.add(clientHandler);
            }
            clientHandler.start();
        }
    }

    public void createNewGame(String playerId, String gameName){
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        System.out.println("Creation of a game with name " + gameName + " by player " + playerId);
        synchronized (gameControllerList) {
            if(checkName(gameName)){
                GameController controller = new GameController();
                controller.getGame().setGameName(gameName);
                controller.getVirtualView().addClientHandler(clientHandler);
                controller.addPlayer(playerId);
                gameControllerList.add(controller);
                clientHandler.setController(controller);
                (new Thread(() -> {
                    try {
                        controller.gameRunner();
                    } catch (Exception ignored) {
                    }
                })).start();
                System.out.println("Controller of game " + gameName + " started");
                clientHandler.send(new AskNickname(true));
            } else {
                System.out.println(gameName + " is already used");
                clientHandler.send(new AskNewGameName());
            }
        }
    }

    public void selectGame(String playerId, String gameName){
        System.out.println("Selection of a game with name " + gameName + " by player " + playerId);
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        synchronized (gameControllerList) {
            GameController controller = gameControllerList.stream()
                    .filter(c -> c.getGame().getGameName().equalsIgnoreCase(gameName))
                    .findFirst().orElse(null);
            if(controller == null || !controller.getGame().gameAcceptPlayers())
                clientHandler.send(new AskNewGameName());
            else {
                controller.getVirtualView().addClientHandler(clientHandler);
                controller.addPlayer(playerId);
                clientHandler.setController(controller);
                if (controller.getGame().getPlayerHandler().getPlayers().size() == controller.getGame().getPlayerHandler().getNumPlayers()){
                    gameControllerList.remove(controller);
                    System.out.println("game " + controller.getGame().getGameName() + " removed from gameControllerList");
                }
                clientHandler.send(new AskNickname(true));
            }
        }
    }

    private ClientHandler getClientHandlerByPlayerId(String playerId){
        ClientHandler clientHandler;
        clientHandler = clientHandlerList.stream()
                .filter( p -> Objects.equals(p.getPlayerId(), playerId))
                .findFirst().orElse(null);
        // TODO: should it launch an exception if null ?
        return clientHandler;
    }

    public void sendActiveGames(String playerId){
        System.out.println("Sending list of active games to player " + playerId);
        List<GameModel> gameList = gameModelList().stream()
                .filter(GameModel::gameAcceptPlayers)
                .collect(Collectors.toList());
        getClientHandlerByPlayerId(playerId).send(new GameListMessage(GameListInfo.createGameInfoList(gameList)));
    }

    public void removeClientHandlerById(String playerId){
        System.out.println("Disconnection of player " + playerId);
        synchronized (gameControllerList) {
            GameController controllerToRemove = null;
            boolean controllerFound = false;
            for (GameController controller : gameControllerList) {
                for (Player player : controller.getGame().getPlayerHandler().getPlayers()) {
                    if (Objects.equals(player.getId(), playerId)) {
                        controllerFound = true;
                        controllerToRemove = controller;
                        break;
                    }
                }
                if (controllerFound)
                    break;
            }
            if (controllerFound)
                gameControllerList.remove(controllerToRemove);
        }
        synchronized (clientHandlerList) {
            try {
                clientHandlerList.remove(getClientHandlerByPlayerId(playerId));
            } catch (Exception ignored){
            }
        }
    }

    private boolean checkName(String gameName){
        for (GameModel game : gameModelList()){
            if (game.getGameName().equalsIgnoreCase(gameName))
                return false;
        }
        return true;
    }
}
