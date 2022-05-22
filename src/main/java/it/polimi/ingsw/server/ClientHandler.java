package it.polimi.ingsw.server;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.network.SYSMessage;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.network.heartbeat.HeartbeatSender;
import it.polimi.ingsw.network.messages.NotifyPlayerIdMessage;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler extends Thread implements NetworkHandler {
    private final Socket socket;
    private final ServerMain serverMain;
    private VirtualView virtualView;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final Object lock;
    private boolean isFirstPlayer;
    private String playerId;
    private boolean isConnected;

    public ClientHandler(ServerMain serverMain, Socket socket) throws IOException {
        this.socket = socket;
        this.serverMain = serverMain;

        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.lock = new Object();

        this.isConnected = true;
        socket.setSoTimeout(Configurator.getSocketTimeout());
        (new HeartbeatSender(this, true)).start();

        this.playerId = UUID.randomUUID().toString();
        send(new NotifyPlayerIdMessage(playerId));

    }

    public void setVirtualView(VirtualView virtualView){
        this.virtualView = virtualView;
    }

    public void setFirstPlayer(boolean isFirstPlayer){
        this.isFirstPlayer = isFirstPlayer;
    }

    public String getPlayerId() {
        return playerId;
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
                        vcMessage.execute(virtualView.getController());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                if (virtualView != null) {
                    if (isConnected) {
                        // This player has disconnected
                        System.out.println("Warning: player has disconnected during message receiving");
                        isConnected = false;
                        virtualView.setDisconnected(playerId);
                    } else {
                        // Another player has disconnected
                        System.out.println("Status: player was forced to stop during message receiving");
                    }
                }
                serverMain.setDisconnected(playerId);
                close();
            }
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

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
                if (isConnected) {
                    // This player has disconnected
                    System.out.println("> Warning: player has disconnected during message sending");
                    isConnected = false;
                    virtualView.setDisconnected(playerId);
                } else {
                    // Another player has disconnected
                    System.out.println("> Status: player was forced to stop during message sending");
                }
                close();
            }
        }
    }

    private void close(){
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
