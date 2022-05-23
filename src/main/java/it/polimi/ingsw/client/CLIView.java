package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CLIView implements View{
	private Scanner scanner;
	// TODO: se qualcuno scrive mentre non è il tuo turno si riempe buffer, chiedi a pale
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
				serverIP = Configurator.getServerIp();
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
		askLobbyOrNew();
	}

	protected void askLobbyOrNew(){
		boolean correct = false;
		while(!correct){
			correct = true;
			System.out.println(" Do do you want to create a new game or join an existing one? [n/e]");
			String preferredMode = scanner.nextLine();
			if ((preferredMode.equalsIgnoreCase("n"))){
				System.out.println(" Please insert the game name: ");
				String gameName = scanner.nextLine();
				serverHandler.send(new NewGameMessage(playerId, gameName));
			}
			else if ((preferredMode.equalsIgnoreCase("e")))
				serverHandler.send(new AskGameListMessage(playerId));
			else{
				System.out.println(" Please insert a correct value ");
				correct = false;
			}
		}
	}

	@Override
	public void askNewGameName() {
		System.out.println(" The game name is already chosen. Please insert a new one: ");
		String gameName = scanner.nextLine();
		serverHandler.send(new NewGameMessage(playerId, gameName));
	}

	@Override
	public void askNickname(boolean isFirstRequest) {
		if(!isFirstRequest)
			System.out.println("> This nickname is already chosen. Please insert a new one");
		boolean correct;
		do {
			System.out.print(" > Enter your nickname: ");
			nickname = scanner.nextLine();
			correct = InputValidator.isNickname(nickname);
			if (!correct) {
				System.out.println(" > Invalid nickname. Try again.");
			}
		} while (!correct);
		serverHandler.send(new SetNickname(playerId,nickname));
	}

	@Override
	public void askTowerColor(List<TowerColor> possibleColor,boolean isFirstRequest) {
		TowerColor chosenColor;
		String inputColor;
		if (!isFirstRequest)
			System.out.println("> This color is already chosen. Please insert a new one");
		// There's only one color?
		if (possibleColor.size() == 1) {
			chosenColor = possibleColor.get(0);
			System.out.print(" > Your color will be" + chosenColor.toString());
		} else {
			boolean correct;
			do {
				System.out.print(" > Choose your tower color between: ");
				for (int i = 0; i < possibleColor.size(); i++) {
					System.out.print(possibleColor.get(i).toString() + " ");
				}
				System.out.println();
				System.out.print("  ↳: ");
				inputColor = scanner.next();
				chosenColor = TowerColor.getPlayerColorByName(inputColor);
				//correct = InputValidator.isTowerColorBetween(chosenColor,possibleColor);
				//if (!correct) {
					//System.out.println(" > Invalid choice. Try again.");
				//}
				correct = true;
			} while (!correct);
		}
		serverHandler.send(new SetTowerColor(playerId,chosenColor));
	}

	@Override
	public void askGameSettings() {
		boolean correct = false;
		int numPlayers = 0;
		GameMode gameMode = GameMode.BASIC;
		while(!correct){
			System.out.print("> Enter number of player: ");
			scanner = new Scanner(System.in);
			numPlayers = Integer.parseInt(scanner.nextLine());
			if(InputValidator.isNumberBetween(numPlayers,1,4))
				correct = true;
			if (!correct) {
				System.out.println(" > Invalid number. Try again.");
			}
		}
		correct = false;
		// FIXME: loop
		while(!correct){
			System.out.print("> Basic mode? [y/n]: ");
			String preferredMode = scanner.nextLine();
			correct = true;
			if ((preferredMode.equalsIgnoreCase("n")))
				gameMode = GameMode.EXPERT;
		}
		serverHandler.send(new SetGameSettings(playerId, gameMode,numPlayers));
	}


	@Override
	public void askAssistantCard(List<AssistantCard> cards) {
		int chosenID;
		AssistantCard chosenCard;
		if (cards.size() == 1) {
			chosenCard = cards.get(0);
			System.out.print(" > Your card will be " + chosenCard.getCardID());
		} else {
			boolean correct = false;
			do {
				System.out.print(" > Choose your card ID between: ");
				for (int i = 0; i < cards.size(); i++) {
					System.out.print(cards.get(i) + " / ");
				}
				System.out.println();
				System.out.print("  ↳: ");
				chosenID = Integer.parseInt(scanner.next());
				chosenCard = InputValidator.isIDBetween(chosenID,cards);
				if (chosenCard!=null)
					correct = true;
				else
					System.out.println(" > Invalid choice. Try again.");
			} while (!correct);
		}
		serverHandler.send(new SetAssistantCard(nickname,chosenCard));
	}

	@Override
	public void askAction(RoundActions roundActions,boolean isInvalidAction) {
		String action;
		Piece chosenPiece;
		int chosenId;
		Action chosenAction = null;
		boolean correct;
		if(isInvalidAction)
			System.out.println(" > Mossa non valida!");

		do {
			correct = false;
			showPossibleActions(roundActions);
			System.out.print("Insert your action type: [MOVE_STUDENT_ISLAND <msi> / MOVE_STUDENT_ROOM <msr> / MOVE_MOTHER <mm> / CHOOSE_CLOUD <cc>]:");
			action = scanner.next();
			if (action.equalsIgnoreCase("msi")) {
				System.out.print("Insert the student's color [red / blue / green / yellow / pink]: ");
				chosenPiece = Piece.getPieceByColor(scanner.next());
				if(chosenPiece!=null){
					System.out.print("Insert the island ID: ");
					chosenId = Integer.parseInt(scanner.next());
					chosenAction = new Action(ActionType.MOVE_STUDENT_TO_ISLAND, chosenPiece,null,chosenId);
				}else
					System.out.println(" Please insert a valid value for color");
			}
			if (action.equalsIgnoreCase("msr")) {
				System.out.print("Insert the student's color [red / blue / green / yellow / pink]: ");
				chosenPiece = Piece.getPieceByColor(scanner.next());
				if(chosenPiece!=null){
					chosenAction = new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, chosenPiece,null,0);
				}else
					System.out.println(" Please insert a valid value for color");
			}
			if (action.equalsIgnoreCase("mm")){
				System.out.print(" Insert the number of mother nature steps: ");
				chosenId = Integer.parseInt(scanner.next());
				chosenAction = new Action(ActionType.MOVE_MOTHER_NATURE,null,null,chosenId);
			}
			if (action.equalsIgnoreCase("cc")){
				System.out.print("Insert the cloud ID: ");
				chosenId = Integer.parseInt(scanner.next());
				chosenAction = new Action(ActionType.CHOOSE_CLOUD,null,null,chosenId);
			}
			if ((chosenAction == null)||(!roundActions.getActionsList().contains(chosenAction.getActionType()))) {
				System.out.println(" > Invalid action. Try again.");
			}else
				correct = true;
		} while (!correct);
		serverHandler.send(new SetAction(nickname,chosenAction));
	}

	@Override
	public void showGamesList(List<GameListInfo> gamesList) {
		boolean correct;
		String gameName;
		for (GameListInfo gameInfo:gamesList) {
			System.out.println("Game: " + gameInfo.getGameName() + " #Players: " + gameInfo.getNumPlayers() +  " Mode: " + gameInfo.getGameMode());
		}
		System.out.println();
		do {
			correct = false;
			System.out.print("Insert the name of the game you want to join ('.' for updating the list): ");
			gameName = scanner.next();
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

	@Override
	public void askNewGameChoice() {
		System.out.println(" > Invalid choice, the game is full");
		serverHandler.send(new AskGameListMessage(playerId));
	}


	public void showPossibleActions(RoundActions roundActions) {
		System.out.println("Your possible actions are: ");
		for (Action action:roundActions.getActionsList()) {
			System.out.println(action.getActionType().toString() + " / ");
		}
	}


	@Override
	public void showGame(GameModel game) {
		for (Player p : game.getPlayerHandler().getPlayers()) {
			System.out.println(("<===================" + p.getNickname() + "===========================>"));
			System.out.println(" Board ENTRANCE: ");
			for (Piece piece:p.getPlayerBoard().getStudentsEntrance()) {
				System.out.print(piece.getColor() + " ");
			}
			System.out.println();
			System.out.println(" Board DINING ROOM: ");
			for (Piece piece:Piece.values()) {
				System.out.print(piece.getColor() + " x" +  p.getPlayerBoard().getNumOfStudentsRoom(piece));
				if(game.getTeacherHandler().getTeacherOwner(piece).getNickname().equals(p.getNickname()))
					System.out.print(" Teacher ");
				System.out.println();
			}
			System.out.println(" #TOWER "+ p.getNumOfTower());
		}

		System.out.println(("<****************** ISLANDS *********************>"));
		for (Island island : game.getIslandHandler().getIslands()) {
			System.out.println("ID: " + island.getID() + " size: " + island.getSize());
			System.out.println(" Students:  ");
			for (Piece piece:Piece.values()) {
				System.out.print(piece.getColor() + " x" +  island.getNumStudents(piece) + " / ");
			}
			if(island.towerIsAlreadyBuild())
				System.out.println("Presente torre di: " + island.getIslandOwner().getNickname());
		}
	}

	@Override
	public void showTurn(String currentNickname) {
		System.out.println(" It is " + currentNickname +"'s turn");
	}

	@Override
	public void showMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void showQueuedMessage() {
		System.out.println("Waiting other players...");
	}

	@Override
	public void showGameEndMessage(String winnerNickname) {
		System.out.println("Il vincitore è: " + winnerNickname);
	}

	@Override
	public void showErrorMessage(String errorMessage) {
		System.out.println(" > ERROR: "+ errorMessage);
	}
}
