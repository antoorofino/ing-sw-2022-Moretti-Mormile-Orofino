package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.util.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CLIView implements View{
	private final Scanner scanner;
	private ServerHandler serverHandler;
	private String playerId;
	private String nickname;
	private GameListInfo gameInfo;
	private int currentRow;
	private int currentColumn;

	public CLIView() {
		this.scanner = new Scanner(System.in);
	}

	@Override
	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

	@Override
	public void run() {
		clear();
		String serverIP;
		String serverPort;
		int portNumber = 0;
		boolean correct = false;
		CLIGameSettings.drawTitle(AnsiColor.ANSI_BRIGHT_BLUE,AnsiBackColor.ANSI_DEFAULT);
		currentRow = 21;
		currentColumn = 64;
		do {
			setCursor();
			System.out.print(CLIFrmt.println('i','d'," Enter the server IP [press enter for default IP]: "));
			serverIP = scanner.nextLine();
			setCursor();
			if (serverIP.isEmpty()) {
				correct = true;
				serverIP = Configurator.getServerIp();
			}else{
				if (!InputValidator.isIp(serverIP)){
					showErrorMessage("Invalid IP. Try again.");
				}
				else
					correct = true;
			}
		}while(!correct);
		correct = false;
		do {
			System.out.print(" Enter the server port [press enter for default port]: ");
			serverPort = scanner.nextLine();
			setCursor();
			if (serverPort.isEmpty()) {
				correct = true;
				portNumber = Configurator.getServerPort();
			} else {
				if (InputValidator.isPortNumber(serverPort)) {
					correct = true;
					portNumber = Integer.parseInt(serverPort);
				} else {
					showErrorMessage("Invalid port number. Try again.");
					setCursor();
				}
			}
		} while (!correct);
		serverHandler.setConnection(serverIP, portNumber);
	}

	@Override
	public void setPlayerId(String playerId) {
		//System.out.println(" Your player identifier is: " +  playerId);
		this.playerId = playerId;
		askLobbyOrNew();
	}

	private void askLobbyOrNew(){
		boolean correct;
		do {
			correct = true;
			System.out.print(" Do do you want to create a new game or join an existing one? [n/e]: ");
			String preferredMode = scanner.nextLine();
			setCursor();
			if ((preferredMode.equalsIgnoreCase("n"))){
				askGameSettings();
			} else if ((preferredMode.equalsIgnoreCase("e"))) {
				serverHandler.send(new AskGameListMessage(playerId));
			} else {
				showErrorMessage("Please insert a correct value");
				setCursor();
				correct = false;
			}
		} while (!correct);
	}

	private void askGameSettings(){
		boolean correct = false;
		GameMode gameMode = null;
		String gameName ;
		int numPlayers = 0;
		do {
			System.out.print(" Please insert the game name: ");
			gameName = scanner.nextLine();
			setCursor();
			if (InputValidator.isWordNotEmpty(gameName)) {
				correct = true;
			} else {
				showErrorMessage("Please insert a correct value");
				setCursor();
			}
		} while (!correct);
		correct = false;
		do {
			System.out.print(" Enter number of players: ");
			numPlayers = getNumber();
			setCursor();
			if (InputValidator.isNumberBetween(numPlayers, 2, 3))
				correct = true;
			else{
				showErrorMessage("Invalid value. Try again.");
				setCursor();
			}
		} while (!correct);
		correct = false;
		do {
			System.out.print(" Basic mode? [y/n]: ");
			String preferredMode = scanner.nextLine();
			setCursor();
			if ((preferredMode.equalsIgnoreCase("n"))) {
				correct = true;
				gameMode = GameMode.EXPERT;
			} else if ((preferredMode.equalsIgnoreCase("y"))) {
				correct = true;
				gameMode = GameMode.BASIC;
			} else {
				showErrorMessage("Invalid value. Try again.");
				setCursor();
			}
		} while (!correct);
		this.gameInfo = new GameListInfo(gameName,gameMode,numPlayers);
		serverHandler.send(new NewGameMessage(playerId, gameInfo));
	}

	@Override
	public void askNewGameName() {
		boolean correct = false;
		do {
			System.out.print(" The game name is already chosen. Please insert a new one: ");
			String gameName = scanner.nextLine();
			setCursor();
			if (InputValidator.isWordNotEmpty(gameName)) {
				correct = true;
				serverHandler.send(new NewGameMessage(playerId, new GameListInfo(gameName,gameInfo.getGameMode(),gameInfo.getNumPlayers())));
			} else {
				showErrorMessage("Please insert a correct value");
				setCursor();
			}
		} while (!correct);
	}

	@Override
	public void askNickname(boolean isFirstRequest) {
		if(!isFirstRequest){
			System.out.println(" This nickname is already chosen. Please insert a new one.");
			setCursor();
		}
		boolean correct = false;
		do {
			System.out.print(" Enter your nickname: ");
			nickname = scanner.nextLine();
			setCursor();
			if (InputValidator.isWordNotEmpty(nickname)) {
				correct = true;
			} else {
				showErrorMessage("Invalid nickname. Try again.");
				setCursor();
			}
		} while (!correct);
		serverHandler.send(new SetNickname(playerId, nickname));
	}

	@Override
	public void askTowerColor(List<TowerColor> possibleColor, boolean isFirstRequest) {
		TowerColor chosenColor;
		String inputColor;
		if (!isFirstRequest) {
			System.out.println(" This color is already chosen. Please insert a new one");
			setCursor();
		}
		if (possibleColor.size() == 1) {
			chosenColor = possibleColor.get(0);
			System.out.println(" Your color will be " + chosenColor.toString());
			setCursor();
		} else {
			boolean correct;
			do {
				System.out.print(" Choose your tower color between: ");
				for (TowerColor towerColor : possibleColor) {
					System.out.print(towerColor.toString() + " ");
				}
				setCursor();
				System.out.print(" -> ");
				inputColor = scanner.nextLine();
				setCursor();
				chosenColor = TowerColor.getPlayerColorByName(inputColor);
				if (chosenColor == null)
					correct = false;
				else
					correct = InputValidator.isTowerColorBetween(chosenColor,possibleColor);
				if (!correct) {
					showErrorMessage("Invalid choice. Try again.");
					setCursor();
				}
			} while (!correct);
		}
		serverHandler.send(new SetTowerColor(playerId,chosenColor));
	}


	@Override
	public void askAssistantCard(List<AssistantCard> cards,GameModel game) {
		int chosenID;
		AssistantCard chosenCard = null;
		printGame(game,cards);
		if (cards.size() == 1) {
			chosenCard = cards.get(0);
			System.out.println(" Your card will be " + chosenCard.getCardID());
		} else {
			boolean correct = false;
			do {
				System.out.println(" Insert your card value: ");
				chosenID = getNumber();
				chosenCard = InputValidator.isIDBetween(chosenID, cards);
				if (chosenCard != null){
					System.out.println(" Chosen card: "+ chosenCard);
					correct = true;
				} else {
					showErrorMessage("Invalid choice. Try again.");
				}
			} while (!correct);
		}
		serverHandler.send(new SetAssistantCard(nickname,chosenCard));
	}

	@Override
	public void askAction(RoundActions roundActions, boolean isInvalidAction) {
		int num;
		Piece chosenPiece;
		Piece secondPiece;
		int integer;
		Action chosenAction = null;
		ActionType action = ActionType.END;
		boolean correct = false;
		if(isInvalidAction)
			showErrorMessage("Invalid action. Try again.");
		if (roundActions.getActionsList().size() == 1) { // only one possible action
			action = roundActions.getActionsList().get(0).getActionType();
			correct = true;
		}
		while(!correct) {
			showPossibleActions(roundActions);
			System.out.print(" -> ");
			num = getNumber();
			if (InputValidator.isValidAction(num, roundActions)) {
				action = roundActions.getActionsList().get(num).getActionType();
				correct = true;
			}
		}

		switch(action){
			case MOVE_STUDENT_TO_ISLAND:
			case STUDENT_FROM_CARD_TO_ISLAND:
				chosenPiece = getColorInput(" Insert the student's color [red / blue / green / yellow / purple]: ");
				integer = getNumber(" Insert the island ID: ");
				chosenAction = new Action(action, chosenPiece,null, integer);
				break;
			case MOVE_STUDENT_TO_DININGROOM:
			case COLOR_NO_INFLUENCE:
			case STUDENT_FROM_CARD_TO_DINING:
			case STUDENT_FROM_DINING_TO_BAG:
				chosenPiece = getColorInput(" Insert the student's color [red / blue / green / yellow / purple]: ");
				chosenAction = new Action(action, chosenPiece,null,0);
				break;
			case MOVE_MOTHER_NATURE:
				integer = getNumber(" Insert the number of mother nature steps: ");
				chosenAction = new Action(action,null,null, integer);
				break;
			case CHOOSE_CLOUD:
				integer = getNumber(" Insert the cloud ID: ");
				chosenAction = new Action(action,null,null,integer);
				break;
			case CHOOSE_CHARACTER:
				integer = getNumber(" Insert the character ID to activate: ");
				chosenAction = new Action(action,null,null,integer);
				break;
			case DOUBLE_INFLUENCE:
			case NO_INFLUENCE:
				integer = getNumber(" Insert the island ID: ");
				chosenAction = new Action(action,null,null,integer);
				break;
			case STUDENT_FROM_CARD_TO_ENTRANCE:
				if(wantToContinue()) {
					chosenPiece = getColorInput(" Insert the student's color to remove from card [red / blue / green / yellow / purple]: ");
					secondPiece = getColorInput(" Insert the student's color to remove from entrance [red / blue / green / yellow / purple]: ");
					chosenAction = new Action(action, chosenPiece, secondPiece, 0);
				}else
					chosenAction = new Action(action, null,null,-1);
				break;
			case STUDENT_FROM_ENTRANCE_TO_DINING:
				if(wantToContinue()){
					chosenPiece = getColorInput(" Insert the student's color to remove from entrance [red / blue / green / yellow / purple]: ");
					secondPiece = getColorInput(" Insert the student's color to remove from dining [red / blue / green / yellow / purple]: ");
					chosenAction = new Action(action, chosenPiece,secondPiece,0);
				}else
					chosenAction = new Action(action, null,null,-1);
				break;
		}
		serverHandler.send(new SetAction(nickname, chosenAction));
	}

	private boolean wantToContinue(){
		String response;
		while(true) {
			System.out.print(" Do you want to continue the action? [y/n]: ");
			response = scanner.nextLine();
			if ((response.equalsIgnoreCase("n"))){
				return false;
			} else if ((response.equalsIgnoreCase("y")))
				return true;
			else
				showErrorMessage("Please insert a valid answer");
		}
	}

	private Piece getColorInput(String string) {
		Piece piece;
		while(true) {
			System.out.print(string);
			piece = Piece.getPieceByColor(scanner.nextLine());
			if(piece != null)
				return piece;
			else
				showErrorMessage("Please insert a valid value for color");
		}
	}

	private int getNumber(String req){
		System.out.println(req);
		return getNumber();
	}

	private int getNumber() {
		int num;
		while (true) {
			String stringRead = scanner.nextLine();
			if (!InputValidator.isNumber(stringRead))
				showErrorMessage("Please insert a valid number");
			else {
				num = Integer.parseInt(stringRead);
				return num;
			}
		}
	}

	@Override
	public void showGamesList(List<GameListInfo> gamesList) {
		boolean correct = false;
		String gameName;
		for (GameListInfo gameInfo:gamesList) {
			System.out.println(" Game: " + gameInfo.getGameName() + " #Players: " + gameInfo.getNumPlayers() +  " Mode: " + gameInfo.getGameMode());
		}
		do {
			System.out.print(" Insert the name of the game you want to join ('.' for updating the list): ");
			gameName = scanner.nextLine();
			if(Objects.equals(gameName, ".")){
				serverHandler.send(new AskGameListMessage(playerId));
				return;
			}
			if(InputValidator.isGameName(gameName,gamesList))
				correct = true;
			else
				System.out.println(" Please insert a valid value for game name");
		} while (!correct);
		serverHandler.send(new SelectGameMessage(playerId,gameName));
	}

	/**
	 * @param game the game model
	 * @param firstPlayerNickname the nickname of first player to play
	 */
	@Override
	public void showGameStart(GameModel game, String firstPlayerNickname) {
		System.out.println(" The players will be " + game.getPlayerHandler().getPlayersNickName());
		System.out.println("Press ENTER to continue");
		scanner.nextLine();
		printGame(game,null);
	}

	@Override
	public void askNewGameChoice() {
		System.out.println(" The game is already full");
		serverHandler.send(new AskGameListMessage(playerId));
	}

	private void showPossibleActions(RoundActions roundActions) {
		System.out.println(" Your possible actions are: ");
		int i = 0;
		for (Action action:roundActions.getActionsList()) {
			System.out.println(" " + i + "." + action.getActionType().getDescription());
			i++;
		}
	}

	@Override
	public void showGame(GameModel game) {
		clear();
		printGame(game,null);
	}


	@Override
	public void showLastRound() {
		System.out.println("Last round");
	}

	@Override
	public void showQueuedMessage() {
		System.out.println(" Waiting other players...");
	}

	@Override
	public void showGameEndMessage(String winnerNickname) {
		System.out.println(" Match finished. The winner is: " + winnerNickname);
	}

	@Override
	public void showConnectionErrorMessage() {
		System.out.println(" Error during the message sending/receiving" );
	}

	@Override
	public void showDisconnection(String playerDisconnected) {
		showErrorMessage("Player " + playerDisconnected + " disconnected from the game");
		serverHandler.close();
	}

	@Override
	public void showErrorOnConnection() {
		showErrorMessage("Unable to connect to the server");
	}

	private void showErrorMessage(String errorMessage) {
		System.out.println(" Error: "+ errorMessage);
	}

	protected void printGame(GameModel game, List<AssistantCard> possibleCards){
		CLIGame cliGame = new CLIGame(game,possibleCards,playerId);
		cliGame.display();

		if (Objects.equals(game.getPlayerHandler().getCurrentPlayer().getId(), playerId))
			System.out.println(" -> It is your turn");
		else
			System.out.println(" -> It is " + game.getPlayerHandler().getCurrentPlayer().getNickname() +"'s turn");
		// TODO: non mi convince
		try {
			System.in.read(new byte[System.in.available()]);
		} catch (IOException ignored) {
		}
	}

	private void clear(){
		System.out.print("\033[H\033[2J"); // clear screen
	}

	private void setCursor(){
		currentRow++;
		System.out.print("\u001b[" + currentRow +";" + currentColumn + "H"); // muove cursore
	}
}
