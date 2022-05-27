package it.polimi.ingsw.client;

import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.network.VCMessage;
import it.polimi.ingsw.network.heartbeat.HeartbeatSender;
import it.polimi.ingsw.util.Configurator;
import it.polimi.ingsw.util.MessageType;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements NetworkHandler {
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Socket socket;
	private View view;
	private final Object lock;
	private boolean isConnected;

	public ServerHandler(View view){
		this.lock = new Object();
		this.isConnected = false;
		this.view = view;
	}

	/**
	 * Sets connection to the specified server
	 *
	 * @param serverIP IP address of the server
	 */
	public void setConnection(String serverIP,int port) throws IOException {
		socket = new Socket(serverIP, port);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		isConnected = true;

		socket.setSoTimeout(Configurator.getSocketTimeout());
		(new HeartbeatSender(this, false)).start();
		startListening();
	}

	/**
	 * Starts listening for server messages and execute them client-side
	 */

	public void startListening() {
		while (isConnected) {
			try {
				CVMessage serverMessage = (CVMessage) input.readObject();
				serverMessage.execute(view);
			} catch (IOException | ClassNotFoundException e) {
				System.out.println(e.getMessage());
				if (isConnected) {
					view.showErrorMessage("Server unreachable");
				}
				isConnected = false;
			}
		}
	}

	/**
	 * Checks the connection status
	 *
	 * @return True if it is connected, otherwise false
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Sends a message
	 *
	 * @param message The message to be sent
	 */
	public void send(Message message) {
		if (isConnected) {
			synchronized (lock){
				try {
					output.writeUnshared(message);
					output.flush();
					output.reset();
				} catch (IOException e) {
					view.showErrorMessage("> Server unreachable");
					close();
				}
			}
		}
	}

	/**
	 * Closes the connection to the server
	 */
	public void close(){
		try {
			socket.close();
			isConnected = false;
		} catch (IOException e) {
			view.showErrorMessage("> An error occurred when closing the connection");
			e.printStackTrace();
		}
	}
}