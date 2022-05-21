package it.polimi.ingsw.client;

import java.util.Scanner;

public class ClientMain {
	public static void main(String[] args) {
		ClientMain clientMain = new ClientMain();
		clientMain.launch();
	}

	/**
	 * ClientLauncher launcher. Asks the preferred UI and launches it.
	 */
	private void launch() {
		Scanner scanner = new Scanner(System.in);
		ServerHandler serverHandler;
		View view;
		boolean incorrect = true;

		do {
			System.out.print(" Choose the interface you want to use [CLI/GUI]: ");
			String preferredInterface = scanner.nextLine();
			if ((preferredInterface.equalsIgnoreCase("CLI"))) {
				view = new CLIView();
				serverHandler = new ServerHandler(view);
				view.setServerHandler(serverHandler);
				incorrect = false;
				view.launch();
			}
		} while (incorrect);
	}
}
