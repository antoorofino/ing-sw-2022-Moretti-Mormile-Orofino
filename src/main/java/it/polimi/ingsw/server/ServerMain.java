package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameModel;
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
import java.util.Optional;
import java.util.stream.Collectors;

public class ServerMain {
    private final ServerSocket serverSocket;
    private final List<ClientHandler> clientHandlers;
    private final List<VirtualView> virtualViewList;

    public ServerMain() throws IOException {
        serverSocket = new ServerSocket(Configurator.getServerPort());
        clientHandlers = new ArrayList<>();
        virtualViewList = new ArrayList<>();
    }

    private List<GameModel> gameModelList(){
        return virtualViewList.stream()
                .map(VirtualView::getController)
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
            serverMain.launch();
        } catch (IOException e) {
            System.out.println("Fatal error: Could not connect the client");
        }
    }

    public void launch() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(this, socket);
            synchronized (clientHandlers) {
                clientHandlers.add(clientHandler);
            }
            clientHandler.start();
        }
    }

    public void createNewGame(String playerId, String gameName){
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        if(checkName(gameName)) {
            System.out.println(" A player wants to create a new game " + playerId);
            GameModel game = new GameModel();
            GameController controller = new GameController(game);
            VirtualView virtualView = new VirtualView(controller);
            virtualView.addClientHandler(clientHandler);
            controller.setVirtualView(virtualView);
            synchronized (virtualViewList) {
                virtualViewList.add(virtualView);
            }
            (new Thread(() -> {
                try {
                    controller.gameRunner();
                } catch (Exception ignored) {
                }
            })).start();
            clientHandler.send(new AskNickname(true));
        } else {
            clientHandler.send(new AskNewGameName());
        }
    }

    // TODO: change GameModel to String gameName
    public void selectGame(String playerId, GameModel game){
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        Optional<VirtualView> virtualView = virtualViewList.stream()
                .filter( vv -> vv.containsGame(game))
                .findFirst();
        if(virtualView.isPresent()){
            virtualView.get().addClientHandler(clientHandler);
            synchronized (clientHandlers) {
                clientHandlers.remove(clientHandler);
            }
            if (game.getPlayerHandler().getPlayers().size() + 1  == game.getPlayerHandler().getNumPlayers())
                removeAllPlayers(virtualView.get());
        }
        // TODO: add check whether the game is already full
        clientHandler.send(new AskNickname(true));
        // clientHandler.send(new AskNewGameChoice());
    }

    private void removeAllPlayers(VirtualView virtualView){
        synchronized (virtualViewList) {
            virtualViewList.remove(virtualView);
        }
        synchronized (clientHandlers) {
            for(ClientHandler clientHandler : virtualView.getClientHandlers())
                clientHandlers.remove(clientHandler);
        }
    }

    private ClientHandler getClientHandlerByPlayerId(String playerId){
        ClientHandler clientHandler;
        synchronized (clientHandlers){
            clientHandler = clientHandlers.stream()
                    .filter( p -> Objects.equals(p.getPlayerId(), playerId))
                    .findFirst().orElse(null);
        }
        return clientHandler;
    }

    public void sendActiveGames(String playerId){
        List<GameModel> gameList = gameModelList().stream()
                .filter(GameModel::gameAcceptPlayers)
                .collect(Collectors.toList());
        getClientHandlerByPlayerId(playerId).send(new GameListMessage(GameListInfo.createGameInfoList(gameList)));
    }

    public void setDisconnected(String playerId){
        ClientHandler clientHandler = getClientHandlerByPlayerId(playerId);
        synchronized (clientHandlers){
            clientHandlers.remove(clientHandler);
        }
        Optional<VirtualView> virtualView = virtualViewList.stream()
                .filter( vv -> vv.containsPlayerById(playerId))
                .findFirst();
        if(virtualView.isPresent()){
            synchronized (virtualView.get().getController().getGame()) {
                virtualView.get().getController().getGame().setAsInactive();
            }
            synchronized (virtualViewList) {
                virtualViewList.remove(virtualView.get());
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