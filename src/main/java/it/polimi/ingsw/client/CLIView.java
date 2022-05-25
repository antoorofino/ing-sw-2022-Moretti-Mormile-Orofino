package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
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
					System.out.println(" Invalid IP. Try again.");
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
			System.out.print(" Do do you want to create a new game or join an existing one? [n/e]: ");
			String preferredMode = scanner.nextLine();
			if ((preferredMode.equalsIgnoreCase("n"))){
				System.out.print(" Please insert the game name: ");
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
		System.out.print(" The game name is already chosen. Please insert a new one: ");
		String gameName = scanner.nextLine();
		serverHandler.send(new NewGameMessage(playerId, gameName));
	}

	@Override
	public void askNickname(boolean isFirstRequest) {
		if(!isFirstRequest)
			System.out.println(" This nickname is already chosen. Please insert a new one.");
		boolean correct;
		do {
			System.out.print(" Enter your nickname: ");
			nickname = scanner.nextLine();
			correct = InputValidator.isNickname(nickname);
			if (!correct) {
				System.out.println(" Invalid nickname. Try again.");
			}
		} while (!correct);
		serverHandler.send(new SetNickname(playerId,nickname));
	}

	@Override
	public void askTowerColor(List<TowerColor> possibleColor,boolean isFirstRequest) {
		TowerColor chosenColor;
		String inputColor;
		if (!isFirstRequest)
			System.out.println(" This color is already chosen. Please insert a new one");
		// There's only one color?
		if (possibleColor.size() == 1) {
			chosenColor = possibleColor.get(0);
			System.out.println(" Your color will be " + chosenColor.toString());
		} else {
			boolean correct;
			do {
				System.out.print(" Choose your tower color between: ");
				for (int i = 0; i < possibleColor.size(); i++) {
					System.out.print(possibleColor.get(i).toString() + " ");
				}
				System.out.println();
				System.out.print(" ↳: ");
				inputColor = scanner.nextLine();
				chosenColor = TowerColor.getPlayerColorByName(inputColor);
				correct = InputValidator.isTowerColorBetween(chosenColor,possibleColor);
				if (!correct) {
					System.out.println(" Invalid choice. Try again.");
				}
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
			System.out.print(" Enter number of players: ");
			scanner = new Scanner(System.in);
			numPlayers = Integer.parseInt(scanner.nextLine());
			if(InputValidator.isNumberBetween(numPlayers,1,4))
				correct = true;
			if (!correct) {
				System.out.println(" Invalid number. Try again.");
			}
		}
		correct = false;


		while(!correct){
			System.out.print(" Basic mode? [y/n]: ");
			String preferredMode = scanner.nextLine();
			correct = true;
			if ((preferredMode.equalsIgnoreCase("n")))
				gameMode = GameMode.EXPERT;
			else if ((preferredMode.equalsIgnoreCase("y")))
				gameMode = GameMode.BASIC;
			else
				correct = false;
			if(!correct)
				System.out.println(" Invalid answer. Try again.");
		}
		serverHandler.send(new SetGameSettings(playerId, gameMode,numPlayers));
	}


	@Override
	public void askAssistantCard(List<AssistantCard> cards) {
		int chosenID;
		AssistantCard chosenCard;
		if (cards.size() == 1) {
			chosenCard = cards.get(0);
			System.out.println(" Your card will be " + chosenCard.getCardID());
		} else {
			boolean correct = false;
			do {
				System.out.println(" Choose your card ID between: ");
				for (int i = 0; i < cards.size(); i++) {
					System.out.println(cards.get(i).toString());
				}
				System.out.print(" ↳: ");
				chosenID = Integer.parseInt(scanner.nextLine());
				chosenCard = InputValidator.isIDBetween(chosenID,cards);
				if (chosenCard!=null){
					System.out.println(" Chosen card: "+ chosenCard.toString());
					correct = true;
				}
				else
					System.out.println(" Invalid choice. Try again.");
			} while (!correct);
		}
		serverHandler.send(new SetAssistantCard(nickname,chosenCard));
	}

	@Override
	public void askAction(RoundActions roundActions,boolean isInvalidAction) {
		int num;
		Piece chosenPiece;
		int chosenId;
		Action chosenAction = null;
		ActionType action;
		boolean correct;
		if(isInvalidAction)
			System.out.println(" Mossa non valida! ");
		do {
			correct = false;
			showPossibleActions(roundActions);
			System.out.print(" ↳: ");
			num =  Integer.parseInt(scanner.nextLine());
			if(InputValidator.isValidAction(num,roundActions)){
				correct = true;
				action = roundActions.getActionsList().get(num).getActionType();
				switch(action){
					case MOVE_STUDENT_TO_ISLAND:
					case STUDENT_FROM_CARD_TO_ISLAND:
						chosenPiece = getColorInput();
						System.out.print(" Insert the island ID: ");
						chosenId = Integer.parseInt(scanner.nextLine());
						chosenAction = new Action(action, chosenPiece,null,chosenId);
						break;
					case MOVE_STUDENT_TO_DININGROOM:
					case STUDENT_FROM_CARD_TO_ENTRANCE:
					case COLOR_NO_INFLUENCE:
					case STUDENT_FROM_ENTRANCE_TO_DINING:
					case STUDENT_FROM_CARD_TO_DINING:
					case STUDENT_FROM_DINING_TO_BAG:
						chosenPiece = getColorInput();
						chosenAction = new Action(action, chosenPiece,null,0);
						break;
					case MOVE_MOTHER_NATURE:
					case DOUBLE_INFLUENCE:
						System.out.print(" Insert the number of mother nature steps: ");
						chosenId = Integer.parseInt(scanner.nextLine());
						chosenAction = new Action(action,null,null,chosenId);
						break;
					case CHOOSE_CLOUD:
						System.out.print(" Insert the cloud ID: ");
						chosenId = Integer.parseInt(scanner.nextLine());
						chosenAction = new Action(action,null,null,chosenId);
						break;
					case CHOOSE_CHARACTER:
						System.out.print(" Insert the character ID to activate: ");
						chosenId = Integer.parseInt(scanner.nextLine());
						chosenAction = new Action(action,null,null,chosenId);
						break;
					case NO_INFLUENCE:
						System.out.print(" Insert the island ID: ");
						chosenId = Integer.parseInt(scanner.nextLine());
						chosenAction = new Action(action,null,null,chosenId);
						break;
				}
			}else
				System.out.println(" Invalid action. Try again.");
		} while (!correct);
		serverHandler.send(new SetAction(nickname,chosenAction));
	}

	protected Piece getColorInput(){
		Piece piece;
		while(true){
			System.out.print(" Insert the student's color [red / blue / green / yellow / purple]: ");
			piece = Piece.getPieceByColor(scanner.nextLine());
			if(piece!=null)
				return piece;
			else
				System.out.println(" Please insert a valid value for color");
		}
	}

	@Override
	public void showGamesList(List<GameListInfo> gamesList) {
		boolean correct;
		String gameName;
		for (GameListInfo gameInfo:gamesList) {
			System.out.println(" Game: " + gameInfo.getGameName() + " #Players: " + gameInfo.getNumPlayers() +  " Mode: " + gameInfo.getGameMode());
		}
		do {
			correct = false;
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

	@Override
	public void askNewGameChoice() {
		System.out.println(" Invalid choice, the game is full");
		serverHandler.send(new AskGameListMessage(playerId));
	}


	public void showPossibleActions(RoundActions roundActions) {
		System.out.println(" Your possible actions are: ");
		int i = 0;
		for (Action action:roundActions.getActionsList()) {
			System.out.println(" " + i + "." + action.getActionType().getDescription());
			i++;
		}
	}


	@Override
	public void showGame(GameModel game) {
		for (Player p : game.getPlayerHandler().getPlayers()) {
			System.out.println((" ================== " + p.getNickname() + " =========================="));
			System.out.println(" Board ENTRANCE: ");
			for (Piece piece:p.getPlayerBoard().getStudentsEntrance()) {
				System.out.print(piece.toString() + " ");
			}
			System.out.println();
			System.out.println(" Board DINING ROOM: ");
			for (Piece piece:Piece.values()) {
				System.out.print(piece.toString() + " x" +  p.getPlayerBoard().getNumOfStudentsRoom(piece));
				Player owner = game.getTeacherHandler().getTeacherOwner(piece);
				if(owner!= null)
					if(owner.getNickname().equals(p.getNickname()))
						System.out.print(" Teacher");
				System.out.print(" / ");
			}
			System.out.println();
			System.out.print(" Torri: "+ p.getNumOfTower());
			System.out.println(" Monete: " + p.getCoin());
		}

		System.out.println((" ****************** ISLANDS *********************"));
		int mother = game.getIslandHandler().getMotherNature();
		for (Island island : game.getIslandHandler().getIslands()) {
			System.out.print(" ID: " + island.getID() + " size: " + island.getSize());
			System.out.print(" Students: ");
			for (Piece piece:Piece.values()) {
				System.out.print(piece.toString() + "x" +  island.getNumStudents(piece) + "		");
			}
			if(island.towerIsAlreadyBuild())
				System.out.print("Torre di: " + island.getIslandOwner().getNickname());
			if(island.getID() == mother)
				System.out.print(" Madre natura");
			System.out.println();
		}

		System.out.println((" ################### CLOUDS #################"));
		for (Cloud cloud : game.getClouds()) {
			System.out.println(" ID: " + cloud.getCloudID());
			System.out.print(" Students: ");
			for (Piece piece:cloud.getStudents()) {
				System.out.print(piece.toString() + " ");
			}
			System.out.println();
		}
		System.out.println("@@@@@@@@@@@ CHARACTERS @@@@@@@@@@@@");
		for (Character character : game.getCharacters()) {
			System.out.print(" ID: " + character.getID() + " cost: " + character.getCost() + " no entry: " + character.getIslandFlag());
			System.out.print(" students: ");
			for (Piece piece:character.getStudents()) {
				System.out.print(piece.toString() + " ");
			}
			System.out.println();
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
		System.out.println(" Waiting other players...");
	}

	@Override
	public void showGameEndMessage(String winnerNickname) {
		System.out.println(" Il vincitore è: " + winnerNickname);
	}

	@Override
	public void showErrorMessage(String errorMessage) {
		System.out.println(" Errore: "+ errorMessage);
	}
}
