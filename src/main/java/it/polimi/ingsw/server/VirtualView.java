package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VirtualView {
    private final List<ClientHandler> clientHandlers;
    private final GameController controller;

    public VirtualView(GameController controller) {
        this.controller = controller;
        this.clientHandlers = new ArrayList<>();
    }

    public GameController getController(){
        return controller;
    }

    public void addClientHandler(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.add(clientHandler);
        }
    }

    public ClientHandler getClientHandlerById(String id) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (id.equals(clientHandler.getPlayerId())) {
                return clientHandler;
            }
        }
        return null;
    }

    public void sendToEveryone(Message message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isConnected())
                clientHandler.send(message);
        }
    }

    public void sendToEveryoneExcept(Message message, Player avoidedPlayer) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.getPlayerId().equalsIgnoreCase(avoidedPlayer.getId()) && clientHandler.isConnected()) {
                clientHandler.send(message);
            }
        }
    }

    public void setDisconnected(String playerId) {
        controller.setAsDisconnected(playerId);
    }

    public boolean containsPlayerById(String playerId) {
        for (ClientHandler clientHandler : clientHandlers){
            if (Objects.equals(clientHandler.getPlayerId(), playerId))
                return true;
        }
        return false;
    }

    public boolean containsGame(GameModel game) {
        return controller.getGame().equals(game);
    }

    public void closeAll(){
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isConnected()) {
                clientHandler.setDisconnected();
            }
        }
    }

    public List<ClientHandler> getClientHandlers(){
        return clientHandlers;
    }
}
