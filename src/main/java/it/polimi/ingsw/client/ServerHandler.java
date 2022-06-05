package it.polimi.ingsw.client;

import it.polimi.ingsw.network.CVMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.network.heartbeat.HeartbeatSender;
import it.polimi.ingsw.util.Configurator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements NetworkHandler {
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Socket socket;
	private final View view;
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
	public void setConnection(String serverIP,int port) {
		try {
			socket = new Socket(serverIP, port);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			isConnected = true;

			socket.setSoTimeout(Configurator.getSocketTimeout());
			(new HeartbeatSender(this, false)).start();
			startListening();
		} catch (IOException ignored) {
			view.showErrorOnConnection();
		}
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
				if (isConnected)
					view.showConnectionErrorMessage();
				close();
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
			try {
				synchronized (lock) {
					output.writeUnshared(message);
					output.flush();
					output.reset();
				}
			} catch (IOException ignored) {
				view.showConnectionErrorMessage();
				close();
			}
		}
	}

	/**
	 * Closes the connection to the server
	 */
	public void close(){
		if(socket == null)
			return;
		try {
			isConnected = false;
			socket.close();
		} catch (IOException ignored) {
		}
	}
}