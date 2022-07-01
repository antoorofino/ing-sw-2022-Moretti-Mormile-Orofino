package it.polimi.ingsw.server;

import it.polimi.ingsw.network.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the ClientHandlers of the players connected to the game
 */
public class VirtualView {
    private final List<ClientHandler> clientHandlers;

    /**
     * Constructor: initialises list for client
     */
    public VirtualView() {
        this.clientHandlers = new ArrayList<>();
    }

    /**
     * Adds a client handler
     * @param clientHandler client handler
     */
    public void addClientHandler(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.add(clientHandler);
        }
    }

    /**
     * Gets client handler by id
     * @param id  client handler's id
     * @return specific client handler
     */
    public ClientHandler getClientHandlerById(String id) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (id.equals(clientHandler.getPlayerId())) {
                return clientHandler;
            }
        }
        return null;
    }

    /**
     * Sends a message to everyone
     * @param message message to send
     */
    public void sendToEveryone(Message message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isConnected())
                clientHandler.send(message);
        }
    }

    /**
     * Sends message to specific player by id
     * @param playerId player's id
     * @param message message to send
     */
    public void sendToPlayerId(String playerId, Message message) {
        getClientHandlerById(playerId).send(message);
    }

    /**
     * Closes the connection with all client handler
     */
    public void closeAll(){
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.isConnected()) {
                clientHandler.setDisconnected();
            }
        }
    }
}
