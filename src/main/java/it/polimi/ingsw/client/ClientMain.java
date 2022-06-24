package it.polimi.ingsw.client;

public class ClientMain {
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
