package it.polimi.ingsw.client;

/**
 * Contains the main that starts the client
 */
public class ClientMain {

	/**
	 * Starts the client
	 * @param args -cli to start the cli interface
	 */
	public static void main(String[] args) {
		View view = new GUIView();
		ServerHandler serverHandler;
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-cli"))
				view = new CLIView();
		}
		serverHandler = new ServerHandler(view);
		view.setServerHandler(serverHandler);
		view.run();
	}
}
