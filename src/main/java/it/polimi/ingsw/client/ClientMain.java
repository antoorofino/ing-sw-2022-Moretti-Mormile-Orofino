package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains the main that starts the client
 */
public class ClientMain {
	private static final String HELP_ARGUMENT = "-h";
	private static final String CLI_ARGUMENT = "-cli";


	/**
	 * Starts the client
	 * @param args -cli to start the cli interface
	 */
	public static void main(String[] args) {
		List<String> arguments = new ArrayList<>(Arrays.asList(args));
		if (arguments.size() == 1 && arguments.contains(HELP_ARGUMENT)) {
			String helpMessage = "\nThis is the client interface for Eriantys game, with no arguments provided GUI will start\n\n" +
					"Available argument options:\n" +
					"-h: to get help\n" +
					"-cli: to start the game on command line\n";
			System.out.println(helpMessage);
			return;
		}
		boolean cli;
		if (arguments.contains(CLI_ARGUMENT)) {
			cli = true;
			arguments.remove(CLI_ARGUMENT);
		} else {
			cli = false;
		}
		if(arguments.size() > 0) {
			System.out.println("Invalid arguments provided. Use '-h' to find help");
			return;
		}
		View view;
		ServerHandler serverHandler;
		if (cli)
			view = new CLIView();
		else
			view = new GUIView();
		serverHandler = new ServerHandler(view);
		view.setServerHandler(serverHandler);
		view.run();
	}
}
