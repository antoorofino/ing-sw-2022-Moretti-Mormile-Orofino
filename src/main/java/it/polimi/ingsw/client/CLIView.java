package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.messages.NewGameMessage;
import it.polimi.ingsw.util.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CLIView implements View{
	private final Scanner scanner;
	private Thread inputOutOfTurn;
	private boolean isYourTurn;
	private AtomicBoolean myTurn = new AtomicBoolean(false);
	private ServerHandler serverHandler;
	private String playerId;
	private String nickname;

	public CLIView() {
		this.scanner = new Scanner(System.in);
	}

	@Override
	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

	@Override
	public void launch() {
		String serverIP;
		int port;
		boolean correct = false;

		do {
			System.out.print(" Enter the server IP [press enter for default IP]: ");
			serverIP = scanner.nextLine();
			if (InputValidator.isEmptyIp(serverIP)) {
				correct = true;
				serverIP = "127.0.0.1";
			}else{
				if (!InputValidator.isIp(serverIP))
					System.out.println("  > Invalid IP. Try again.");
				else
					correct = true;
			}
		}while(!correct);

		System.out.print(" Enter the server port [press enter for default port]: ");
		try{
			port = Integer.parseInt(scanner.nextLine());
		}catch(NumberFormatException e){
			port = Configurator.getServerPort();
		}
		serverHandler.setConnection(serverIP,port);
	}

	@Override
	public void setPlayerId(String playerId) {
		System.out.println(" Your player identifier is: " +  playerId);
		this.playerId = playerId;
		askLobbyorNew();

	}

	protected void askLobbyorNew(){
		System.out.println(" Do do you want to create a new game or join an existing one?");
		// TODO implements choice
		serverHandler.send(new NewGameMessage(playerId));
	}

	@Override
	public void askGameSettings(boolean newGame) { // new Game return also
		boolean correct = false;
		int numPlayers = 0;
		String nickname = askNickname();
		if(!newGame) {
			//serverHandler.send(new PlayerNickname(playerId,nickname));
			return;
		}
		while(!correct){
			System.out.print("> Enter number of player: ");
			numPlayers = Integer.parseInt(scanner.nextLine());
			//InputValidator
			if((numPlayers>1)&&(numPlayers<4))
				correct = true;
			if (!correct) {
				System.out.println(" > Invalid number. Try again.");
			}
		}
		//serverHandler.send(new GameSettings(playerId,nickname,numPlayers,gameMode));
	}

	private String askNickname() {
		boolean correct;
		String nickname;
		do {
			System.out.print(" > Enter your nickname: ");
			nickname = scanner.nextLine();
			correct = InputValidator.isNickname(nickname);
			if (!correct) {
				System.out.println(" > Invalid nickname. Try again.");
			}
		} while (!correct);
		return nickname;
	}

	@Override
	public void askNewNickname() {
		// invalid nickname
	}

	@Override
	public void askPlayerColor(ArrayList<String> possibleColor) {
		String chosenColor;
		//There's only one color?
		if (possibleColor.size() == 1) {
			chosenColor = possibleColor.get(0);
			System.out.print(" > Your color will be" + chosenColor);
		} else {
			boolean correct;
			do {
				System.out.print(" > Choose your color between: ");
				for (int i = 0; i < possibleColor.size(); i++) {
					String color = possibleColor.get(i);
					System.out.print(color);
				}
				System.out.println();
				System.out.print("  ↳: ");
				chosenColor = scanner.next();
				correct = InputValidator.isColorBetween(chosenColor,possibleColor);
				if (!correct) {
					System.out.println(" > Invalid choice. Try again.");
				}
			} while (!correct);
		}
		//serverHandler.send(new PlayerColor(chosenColor));
	}

	@Override
	public void askAssistantCard(ArrayList<AssistantCard> cards) {
		int chosenID;
		if (cards.size() == 1) {
			chosenID = cards.get(0).getCardID();
			System.out.print(" > Your card will be " + chosenID);
		} else {
			boolean correct = false;
			do {
				System.out.print(" > Choose your card ID between: ");
				for (int i = 0; i < cards.size(); i++) {
					System.out.println(cards.get(i) + " / ");
				}
				System.out.println();
				System.out.print("  ↳: ");
				chosenID = Integer.parseInt(scanner.next());
				// TODO implement input validator
				//correct= inputValidator.isIDbetween(chosenID,possibleCards);
				if (!correct) {
					System.out.println(" > Invalid choice. Try again.");
				}
			} while (!correct);
		}
		//serverHandler.send(new AssistantCardMessage(chosenID));
	}

	@Override
	public void askNewAssistantCard(ArrayList<AssistantCard> cards) {

	}

	@Override
	public void askAction(RoundActions roundActions, GameInfo gameInfo) {

	}

	@Override
	public void showPossibleActions(RoundActions roundActions) {

	}

	@Override
	public void showGame(GameInfo gameInfo, boolean newScreen) {

	}

	@Override
	public void showTurn(String currentNickname) {

	}

	@Override
	public void showMessage(String message, boolean newScreen) {
		System.out.println(message);
	}

	@Override
	public void showQueuedMessage() {

	}

	@Override
	public void showGameEndMessage(String winnerNickname, boolean youWin) {

	}

	@Override
	public void showErrorMessage(String errorMessage, boolean newScreen) {

	}
}
