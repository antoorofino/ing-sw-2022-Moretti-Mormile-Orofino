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
		View view = null;
		boolean incorrect = true;

		do {
			System.out.print(" Choose the interface you want to use [CLI/GUI]: ");
			String preferredInterface = scanner.nextLine();
			if ((preferredInterface.equalsIgnoreCase("CLI"))) {
				view = new CLIView();
				incorrect = false;
			} else if ((preferredInterface.equalsIgnoreCase("GUI"))) {
				view = new GUIView();
				incorrect = false;
			} else {
				System.out.println(" Invalid choice. Try again.");
			}
		} while (incorrect);
		serverHandler = new ServerHandler(view);
		view.setServerHandler(serverHandler);
		view.run();
	}
}
