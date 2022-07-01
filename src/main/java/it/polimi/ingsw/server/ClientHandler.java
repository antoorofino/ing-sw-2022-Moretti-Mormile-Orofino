package it.polimi.ingsw.server;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.network.heartbeat.HeartbeatSender;
import it.polimi.ingsw.network.messages.NotifyPlayerIdMessage;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * Manages the connection with the client
 */
public class ClientHandler extends Thread implements NetworkHandler {
    private final String playerId;
    private final Socket socket;
    private final ServerMain serverMain;
    private GameController controller;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final Object lock;
    private boolean isConnected;
    private final Logger logger;

    /**
     * Constructor: builds server handler
     * @param serverMain the server main
     * @param socket the client socket
     * @param logger class to show formatted messages
     * @throws IOException exception in case of network errors
     */
    public ClientHandler(ServerMain serverMain, Socket socket, Logger logger) throws IOException {
        this.socket = socket;
        this.serverMain = serverMain;
        this.controller = null;

        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.lock = new Object();

        this.isConnected = true;
        socket.setSoTimeout(Configurator.getSocketTimeout());
        (new HeartbeatSender(this, true)).start();

        this.playerId = UUID.randomUUID().toString();
        send(new NotifyPlayerIdMessage(playerId));

        this.logger = logger;
        logger.log(3, 'i', "Start heartbeat thread for client " + playerId);
    }

    /**
     * Returns the player Id
     * @return the player Id
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Sets the game controller
     * @param controller the game controller
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        while(isConnected) {
            try {
                Message clientMessage = (Message) input.readObject();
                switch (clientMessage.getType()) {
                    case SYS: {
                        SYSMessage sysMessage = (SYSMessage) clientMessage;
                        sysMessage.execute(serverMain);
                        break;
                    }
                    case VC: {
                        VCMessage vcMessage = (VCMessage) clientMessage;
                        vcMessage.execute(controller);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if (controller != null) {
                    if (isConnected) {
                        // This player has disconnected
                        logger.log(1, 'w', "Player " + playerId + " has disconnected during message receiving");
                        controller.setAsDisconnected(playerId);
                    } else {
                        // Another player has disconnected
                        logger.log(1, 'w', "Player " + playerId + " was forced to stop during message receiving");
                    }
                }
                isConnected = false;
                serverMain.removeClientHandlerById(playerId , controller);
                close();
            }
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Sets connected to false
     */
    public void setDisconnected() {
        isConnected = false;
    }

    @Override
    public void send(Message message) {
        if (isConnected) {
            try {
                synchronized (lock) {
                    output.writeUnshared(message);
                    output.flush();
                    output.reset();
                }
            } catch (IOException e) {
                if (controller != null) {
                    if (isConnected) {
                        // This player has disconnected
                        logger.log(1, 'w', "Player " + playerId + " has disconnected during message sending");
                        isConnected = false;
                        controller.setAsDisconnected(playerId);
                    } else {
                        // Another player has disconnected
                        logger.log(1, 'w', "Player " + playerId + " was forced to stop during message sending");
                    }
                    close();
                }
            }
        }
    }

    /**
     * Closes the socket
     */
    private void close(){
        try {
            socket.close();
            logger.log(1, 'i', "Socket closed for player " + playerId);
        } catch (IOException ignored) {
        }
    }
}
