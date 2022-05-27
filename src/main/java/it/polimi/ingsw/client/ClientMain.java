package it.polimi.ingsw.client;

import java.io.IOException;
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

		// TODO: use the input validator class
		do {
			System.out.print(" Choose the interface you want to use [CLI/GUI]: ");
			String preferredInterface = scanner.nextLine();
			if ((preferredInterface.equalsIgnoreCase("CLI"))) {
				view = new CLIView();
				serverHandler = new ServerHandler(view);
				view.setServerHandler(serverHandler);
				incorrect = false;
			} else if ((preferredInterface.equalsIgnoreCase("GUI"))) {
				System.out.println(" GUI not implemented yet");
			} else {
				System.out.println(" Invalid choice. Try again.");
			}
		} while (incorrect);
		
		incorrect = true;
		do {
			try {
				view.launch();
				incorrect = false;
			} catch (IOException e) {
				view.showErrorMessage(" Server unreachable");
			}
		} while (incorrect);
	}
}
