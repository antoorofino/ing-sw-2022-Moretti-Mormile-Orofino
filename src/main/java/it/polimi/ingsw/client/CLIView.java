package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.*;
import it.polimi.ingsw.client.cli.util.AnsiBackColor;
import it.polimi.ingsw.client.cli.util.AnsiColor;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Implement the game interface view for the cli
 */
public class CLIView implements View{
	private final Scanner scanner;
	private ServerHandler serverHandler;
	private String playerId;
	private String nickname;
	private GameListInfo gameInfo;
	private ArrayList<Character> gameCharacters;
	private final int center = 64;

	/**
	 * Constructor: build CLIView
	 * create new Scanner from standard input
	 */
	public CLIView() {
		this.scanner = new Scanner(System.in);

	}

	@Override
	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

	@Override
	public void run() {
		clearAll();
		String serverIP;
		String serverPort;
		int portNumber = 0;
		boolean correct = false;
		CLIGame.drawTitle(AnsiColor.ANSI_BRIGHT_BLUE, AnsiBackColor.ANSI_DEFAULT);
		do {
			centeredInput(CLIFrmt.print('b','b'," Enter the server IP [press enter for default IP]: "));
			serverIP = scanner.nextLine();
			if (serverIP.isEmpty()) {
				correct = true;
				serverIP = Configurator.getDefaultServerIp();
			}else{
				if (!InputValidator.isIp(serverIP)){
					showErrorMessage("Invalid IP. Try again.",true,1);
				}
				else
					correct = true;
			}
		}while(!correct);

		correct = false;
		do {
			centeredInput(CLIFrmt.print('b','b'," Enter the server port [press enter for default port]: "));
			serverPort = scanner.nextLine();
			if (serverPort.isEmpty()) {
				correct = true;
				portNumber = Configurator.getDefaultServerPort();
			} else {
				if (InputValidator.isPortNumber(serverPort)) {
					correct = true;
					portNumber = Integer.parseInt(serverPort);
				} else {
					showErrorMessage("Invalid port number. Try again.",true,1);
				}
			}
		} while (!correct);
		serverHandler.setConnection(serverIP, portNumber);
	}

	@Override
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
		askLobbyOrNew();
	}

	/**
	 * Asks to join a game or create one
	 */
	private void askLobbyOrNew(){
		boolean correct;
		do {
			correct = true;
			centeredInput(CLIFrmt.print('i','d'," Do do you want to create a new game or join an existing one? [n/e]: "));
			String preferredMode = scanner.nextLine();
			if ((preferredMode.equalsIgnoreCase("n"))){
				askGameSettings();
			} else if ((preferredMode.equalsIgnoreCase("e"))) {
				serverHandler.send(new AskGameListMessage(playerId));
			} else {
				showErrorMessage("Please insert a correct value",true,1);
				correct = false;
			}
		} while (!correct);
	}

	/**
	 * Asks the settings of the new game: name, number of players and mode
	 */
	private void askGameSettings(){
		boolean correct = false;
		GameMode gameMode = null;
		String gameName ;
		int numPlayers = 0;
		do {
			centeredInput(CLIFrmt.print('d','g'," Please insert the game name: "));
			gameName = scanner.nextLine();
			if (InputValidator.isWordNotEmpty(gameName)) {
				correct = true;
			} else {
				showErrorMessage("Please insert a correct value",true,1);
			}
		} while (!correct);
		correct = false;

		do {
			numPlayers = getNumber(CLIFrmt.print('d','g'," Enter number of players: "),true);
			if (InputValidator.isNumberBetween(numPlayers, 2, 3))
				correct = true;
			else{
				showErrorMessage("Invalid value. Try again.",true,1);
			}
		} while (!correct);
		correct = false;

		do {
			centeredInput(CLIFrmt.print('d','g'," Basic mode? [y/n]: "));
			String preferredMode = scanner.nextLine();
			if ((preferredMode.equalsIgnoreCase("n"))) {
				correct = true;
				gameMode = GameMode.EXPERT;
			} else if ((preferredMode.equalsIgnoreCase("y"))) {
				correct = true;
				gameMode = GameMode.BASIC;
			} else {
				showErrorMessage("Invalid value. Try again.",true,1);
			}
		} while (!correct);
		this.gameInfo = new GameListInfo(gameName,gameMode,numPlayers);
		serverHandler.send(new NewGameMessage(playerId, gameInfo));
	}

	@Override
	public void askNewGameName() {
		boolean correct = false;
		do {
			showErrorMessage(" The game name is already chosen. Please insert a new one: ",true,5);
			String gameName = scanner.nextLine();
			if (InputValidator.isWordNotEmpty(gameName)) {
				correct = true;
				serverHandler.send(new NewGameMessage(playerId, new GameListInfo(gameName,gameInfo.getGameMode(),gameInfo.getNumPlayers())));
			} else {
				showErrorMessage("Please insert a correct value",true,1);
			}
		} while (!correct);
	}

	@Override
	public void askNickname(boolean isFirstRequest) {
		if(!isFirstRequest){
			centeredPrint(" This nickname is already chosen. Please insert a new one.");
		}

		boolean correct = false;
		do {
			centeredInput(" Enter your nickname: ");
			nickname = scanner.nextLine();
			if (InputValidator.isWordNotEmpty(nickname)) {
				correct = true;
			} else {
				showErrorMessage("Invalid nickname. Try again.",true,1);
			}
		} while (!correct);
		serverHandler.send(new SetNickname(playerId, nickname));
	}

	@Override
	public void askTowerColor(List<TowerColor> possibleColor, boolean isFirstRequest) {
		TowerColor chosenColor;
		String inputColor;

		if (!isFirstRequest) {
			centeredPrint(" This color is already chosen. Please insert a new one");
		}
		if (possibleColor.size() == 1) {
			chosenColor = possibleColor.get(0);
			centeredPrint(" Your color will be " + chosenColor.toString());
		} else {
			boolean correct;
			do {
				centeredInput(" Choose your tower color between ( ");
				for (TowerColor towerColor : possibleColor) {
					System.out.print(towerColor.toString() + " ");
				}
				System.out.print(") : ");
				inputColor = scanner.nextLine();
				chosenColor = TowerColor.getPlayerColorByName(inputColor);
				if (chosenColor == null)
					correct = false;
				else
					correct = InputValidator.isTowerColorBetween(chosenColor,possibleColor);
				if (!correct) {
					showErrorMessage("Invalid choice. Try again.",true,1);
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
				chosenID = getNumber(" Insert your card value: ",false);
				chosenCard = InputValidator.isIDBetween(chosenID, cards);
				if (chosenCard != null){
					correct = true;
				} else {
					showErrorMessage("Invalid choice. Try again.",false,1);
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

		if(isInvalidAction){
			clearAction(true);
			showErrorMessage("Invalid action. Try again.",false,0);
		}

		if (roundActions.getActionsList().size() == 1) { // only one possible action
			action = roundActions.getActionsList().get(0).getActionType();
			correct = true;
		}

		while(!correct) {
			showPossibleActions(roundActions);
			num = getNumber( " Enter the number of the action: ",false);
			if (InputValidator.isValidAction(num, roundActions)) {
				action = roundActions.getActionsList().get(num).getActionType();
				if(action != ActionType.INFO_CHARACTER)
					correct = true;
				else{
					// show character info
					for (Character character:gameCharacters) {
						System.out.println(CLIFrmt.print('d','y'," " + character.getID() + ". " + character.getDescription()));
					}
					System.out.println(CLIFrmt.print('d','y'," Press ENTER to continue"));
					scanner.nextLine();
					clearAction(false);
				}
			}else{
				clearAction(true);
				showErrorMessage("Invalid action. Try again.",false,0);
			}
		}

		switch(action){
			case MOVE_STUDENT_TO_ISLAND:
			case STUDENT_FROM_CARD_TO_ISLAND:
				chosenPiece = getColorInput(" Insert the student's color [red / blue / green / yellow / purple]: ");
				integer = getNumber(" Insert the island ID: ",false);
				chosenAction = new Action(action, chosenPiece, integer);
				break;
			case MOVE_STUDENT_TO_DININGROOM:
			case COLOR_NO_INFLUENCE:
			case STUDENT_FROM_CARD_TO_DINING:
			case STUDENT_FROM_DINING_TO_BAG:
				chosenPiece = getColorInput(" Insert the student's color [red / blue / green / yellow / purple]: ");
				chosenAction = new Action(action, chosenPiece);
				break;
			case MOVE_MOTHER_NATURE:
				integer = getNumber(" Insert the number of mother nature steps: ",false);
				chosenAction = new Action(action, integer);
				break;
			case CHOOSE_CLOUD:
				integer = getNumber(" Insert the cloud ID: ",false);
				chosenAction = new Action(action,integer);
				break;
			case CHOOSE_CHARACTER:
				integer = getNumber(" Insert the character ID to activate: ",false);
				chosenAction = new Action(action,integer);
				break;
			case DOUBLE_INFLUENCE:
			case NO_INFLUENCE:
				integer = getNumber(" Insert the island ID: ",false);
				chosenAction = new Action(action,integer);
				break;
			case STUDENT_FROM_CARD_TO_ENTRANCE:
				if(wantToContinue()) {
					chosenPiece = getColorInput(" Insert the student's color to remove from card [red / blue / green / yellow / purple]: ");
					secondPiece = getColorInput(" Insert the student's color to remove from entrance [red / blue / green / yellow / purple]: ");
					chosenAction = new Action(action, chosenPiece, secondPiece);
				}else
					chosenAction = new Action(action,-1);
				break;
			case STUDENT_FROM_ENTRANCE_TO_DINING:
				if(wantToContinue()){
					chosenPiece = getColorInput(" Insert the student's color to remove from entrance [red / blue / green / yellow / purple]: ");
					secondPiece = getColorInput(" Insert the student's color to remove from dining [red / blue / green / yellow / purple]: ");
					chosenAction = new Action(action, chosenPiece,secondPiece);
				}else
					chosenAction = new Action(action,-1);
				break;
		}
		serverHandler.send(new SetAction(nickname, chosenAction));
	}

	/**
	 * Asks if you want to continue the moves of the character cards
	 * @return the answer
	 */
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
				showErrorMessage("Please insert a valid answer",false,1);
		}
	}

	/**
	 * Asks for the color of the piece
	 * @param string contains the message to be printed
	 * @return Piece of chosen color
	 */
	private Piece getColorInput(String string) {
		Piece piece;
		while(true) {
			System.out.print(string);
			piece = Piece.getPieceByColor(scanner.nextLine());
			if(piece != null)
				return piece;
			else
				showErrorMessage("Please insert a valid value for color",false,1);
		}
	}

	/**
	 * Asks for a number
	 * @param string contains the message to be printed
	 * @return selected number
	 */
	private int getNumber(String string,boolean centered){
		int num;
		while (true) {
			if(centered)
				centeredInput(string);
			else
				System.out.print(string);
			String stringRead = scanner.nextLine();
			if (!InputValidator.isNumber(stringRead))
				showErrorMessage("Please insert a valid number",centered,1);
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
			centeredPrint(CLIFrmt.print('d','y',"   Game: " + gameInfo.getGameName() + " #Players: " + gameInfo.getNumPlayers() +  " Mode: " + gameInfo.getGameMode()));
		}
		do {
			centeredInput(" Insert the name of the game you want to join ('.' for updating the list): ");
			gameName = scanner.nextLine();
			if(Objects.equals(gameName, ".")){
				serverHandler.send(new AskGameListMessage(playerId));
				return;
			}
			if(InputValidator.isGameName(gameName,gamesList))
				correct = true;
			else{
				centeredPrint(" Please insert a valid value for game name");
			}
		} while (!correct);
		serverHandler.send(new SelectGameMessage(playerId,gameName));
	}

	@Override
	public void showGameStart(GameModel game, String firstPlayerNickname) {
		centeredPrint(" The players will be " + game.getPlayerHandler().getPlayersNickName());
		centeredPrint(" Press ENTER to continue");
		scanner.nextLine();
		gameCharacters = game.getCharacters();
		printGame(game,null);
	}

	@Override
	public void askNewGameChoice() {
		System.out.println(" The game is already full");
		serverHandler.send(new AskGameListMessage(playerId));
	}

	/**
	 * Print possible actions
	 * @param roundActions contains the list of possible actions
	 */
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
		printGame(game,null);
	}

	@Override
	public void showLastRound() {
		System.out.println("Last round");
	}

	@Override
	public void showQueuedMessage() {
		centeredPrint(" Waiting other players...");
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
		showErrorMessage("Player " + playerDisconnected + " disconnected from the game",false,0);
		serverHandler.close();
	}

	@Override
	public void showErrorOnConnection() {
		showErrorMessage("Unable to connect to the server",true,0);
	}

	/**
	 * Show an error message
	 * @param errorMessage the message to show
	 */
	private void showErrorMessage(String errorMessage, boolean centered,int top) {
		cursorUp(top);
		clearDown();
		cursorUp(top);
		if(centered)
			centeredPrint(CLIFrmt.print('b','r'," Error: "+ errorMessage));
		else
			System.out.println(CLIFrmt.print('b','r'," Error: "+ errorMessage));
	}

	/**
	 * Updates the game status on the screen
	 * @param game the game model
	 * @param possibleCards the assistant cards that the player can use
	 */
	private void printGame(GameModel game, List<AssistantCard> possibleCards){
		clearAll();
		CLIGame cliGame = new CLIGame(game,possibleCards,playerId);
		cliGame.display();

		try {
			System.in.read(new byte[System.in.available()]);
		} catch (IOException ignored) {
		}
	}

	/**
	 * Print center-aligned text
	 * @param text the text to be printed
	 */
	private void centeredPrint(String text){
		System.out.print("\u001b["+ center +"G"); // column = 64
		System.out.println(text);
	}

	/**
	 * Print center-aligned text and go to the next line
	 * @param text the text to be printed
	 */
	private void centeredInput(String text){
		System.out.print("\u001b["+ center +"G");
		System.out.print(text);
	}

	/**
	 * Moves the cursor up n row
	 * @param n number of lines to move
	 */
	private void cursorUp(int n){
		System.out.print("\u001b[" + n + "A");
	}

	/**
	 * Cleans the screen
	 */
	private void clearAll(){
		clearAction(false);
		System.out.print("\033[H\033[3J"); // maybe 3J
		System.out.flush();
	}

	/**
	 * Erase from cursor to end display
	 */
	private void clearDown(){
		System.out.println("\033[0J");
	}

	/**
	 * Delete text under the game board
	 */
	private void clearAction(boolean isError){
		System.out.print("\u001b[3" + ((isError) ? "6" : "5") + ";0H"); // 36 error, 35 otherwise
		clearDown();
		System.out.print("\u001b[3" + ((isError) ? "6" : "5") + ";0H");
	}
}
