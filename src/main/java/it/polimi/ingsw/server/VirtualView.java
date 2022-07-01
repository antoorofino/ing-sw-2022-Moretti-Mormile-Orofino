package it.polimi.ingsw.server;

import it.polimi.ingsw.network.Message;

import java.util.ArrayList;
import java.util.List;
// TODO Javadoc
public class VirtualView {
    private final List<ClientHandler> clientHandlers;

    public VirtualView() {
        this.clientHandlers = new ArrayList<>();
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

    public void sendToPlayerId(String playerId, Message message) {
        getClientHandlerById(playerId).send(message);
    }

    public void closeAll(){
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isConnected()) {
                clientHandler.setDisconnected();
            }
        }
    }
}
