package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
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

	public CLIView() {
		this.scanner = new Scanner(System.in);
	}

	@Override
	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

	@Override
	public void launch() throws IOException {
		String serverIP;
		String serverPort;
		int portNumber;
		boolean correct = false;

		do {
			System.out.print(" Enter the server IP [press enter for default IP]: ");
			serverIP = scanner.nextLine();
			if (serverIP.isEmpty()) {
				correct = true;
				serverIP = Configurator.getServerIp();
			}else{
				if (!InputValidator.isIp(serverIP))
					showErrorMessage("Invalid IP. Try again.");
				else
					correct = true;
			}
		}while(!correct);

		correct = false;
		do {
			System.out.print(" Enter the server port [press enter for default port]: ");
			serverPort = scanner.nextLine();
			if (serverPort.isEmpty()) {
				correct = true;
				portNumber = Configurator.getServerPort();
			} else {
				try {
					portNumber = Integer.parseInt(serverPort);
					if (!InputValidator.isPortNumber(portNumber))
						showErrorMessage("Invalid port number. Try again.");
					else
						correct = true;
				} catch (NumberFormatException e) {
					portNumber = Configurator.getServerPort();
					showErrorMessage("Invalid port number. Try again.");
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
		boolean correct = false;
		do {
			System.out.print(" Do do you want to create a new game or join an existing one? [n/e]: ");
			String preferredMode = scanner.nextLine();
			if ((preferredMode.equalsIgnoreCase("n"))){
				boolean correctName = false;
				do {
					System.out.print(" Please insert the game name: ");
					String gameName = scanner.nextLine();
					// FIXME: can the name of the game contain spaces ?
					if (InputValidator.isWordNotEmpty(gameName)) {
						correct = true;
						correctName = true;
						serverHandler.send(new NewGameMessage(playerId, gameName));
					} else {
						showErrorMessage("Please insert a correct value");
					}
				} while (!correctName);
			} else if ((preferredMode.equalsIgnoreCase("e"))) {
				correct = true;
				serverHandler.send(new AskGameListMessage(playerId));
			} else {
				showErrorMessage("Please insert a correct value");
			}
		} while (!correct);
	}

	@Override
	public void askNewGameName() {
		boolean correct = false;
		do {
			System.out.print(" The game name is already chosen. Please insert a new one: ");
			String gameName = scanner.nextLine();
			if (InputValidator.isWordNotEmpty(gameName)) {
				correct = true;
				serverHandler.send(new NewGameMessage(playerId, gameName));
			} else {
				showErrorMessage("Please insert a correct value");
			}
		} while (!correct);
	}

	@Override
	public void askNickname(boolean isFirstRequest) {
		if(!isFirstRequest)
			System.out.println(" This nickname is already chosen. Please insert a new one.");
		boolean correct = false;
		do {
			System.out.print(" Enter your nickname: ");
			nickname = scanner.nextLine();
			if (InputValidator.isWordNotEmpty(nickname)) {
				correct = true;
			} else {
				showErrorMessage("Invalid nickname. Try again.");
			}
		} while (!correct);
		serverHandler.send(new SetNickname(playerId, nickname));
	}

	@Override
	public void askTowerColor(List<TowerColor> possibleColor, boolean isFirstRequest) {
		TowerColor chosenColor;
		String inputColor;
		if (!isFirstRequest)
			System.out.println(" This color is already chosen. Please insert a new one");
		if (possibleColor.size() == 1) {
			chosenColor = possibleColor.get(0);
			System.out.println(" Your color will be " + chosenColor.toString());
			System.out.println("Press ENTER to continue");
			scanner.nextLine();
		} else {
			boolean correct;
			do {
				System.out.print(" Choose your tower color between: ");
				for (TowerColor towerColor : possibleColor) {
					System.out.print(towerColor.toString() + " ");
				}
				System.out.println();
				System.out.print(" ↳: ");
				inputColor = scanner.nextLine();
				chosenColor = TowerColor.getPlayerColorByName(inputColor);
				if (chosenColor == null)
					correct = false;
				else
					correct = InputValidator.isTowerColorBetween(chosenColor,possibleColor);
				if (!correct) {
					showErrorMessage("Invalid choice. Try again.");
				}
			} while (!correct);
		}
		serverHandler.send(new SetTowerColor(playerId,chosenColor));
	}

	@Override
	public void askGameSettings() {
		boolean correct = false;
		int numPlayers = 0;
		GameMode gameMode = null;
		do {
			System.out.print(" Enter number of players: ");
			try {
				String stringRead = scanner.nextLine();
				numPlayers = Integer.parseInt(stringRead);
				if (InputValidator.isNumberBetween(numPlayers, 2, 3))
					correct = true;
				else
					showErrorMessage("Invalid value. Try again.");
			} catch (NumberFormatException e) {
				showErrorMessage("Invalid value. Try again.");
			}
		} while (!correct);
		correct = false;
		do {
			System.out.print(" Basic mode? [y/n]: ");
			String preferredMode = scanner.nextLine();
			if ((preferredMode.equalsIgnoreCase("n"))) {
				correct = true;
				gameMode = GameMode.EXPERT;
			} else if ((preferredMode.equalsIgnoreCase("y"))) {
				correct = true;
				gameMode = GameMode.BASIC;
			} else {
				showErrorMessage("Invalid value. Try again.");
			}
		} while (!correct);
		serverHandler.send(new SetGameSettings(playerId, gameMode, numPlayers));
	}

	@Override
	public void askAssistantCard(List<AssistantCard> cards) {
		int chosenID;
		AssistantCard chosenCard = null;
		if (cards.size() == 1) {
			chosenCard = cards.get(0);
			System.out.println(" Your card will be " + chosenCard.getCardID());
		} else {
			boolean correct = false;
			do {
				System.out.println(" Choose your card ID between: ");
				for (AssistantCard card : cards) {
					System.out.println(card.toString());
				}
				System.out.print(" ↳: ");
				String chosenIDString = scanner.nextLine();
				try {
					chosenID = Integer.parseInt(chosenIDString);
					chosenCard = InputValidator.isIDBetween(chosenID, cards);
					if (chosenCard != null){
						System.out.println(" Chosen card: "+ chosenCard);
						correct = true;
					} else {
						showErrorMessage("Invalid choice. Try again.");
					}
				} catch (NumberFormatException e) {
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
		ActionType action;
		boolean correct = false;
		if(isInvalidAction)
			showErrorMessage("Invalid action. Try again.");
		do {
			showPossibleActions(roundActions);
			System.out.print(" ↳: ");
			try {
				String numRead = scanner.nextLine();
				num = Integer.parseInt(numRead);
				if(InputValidator.isValidAction(num,roundActions)){
					correct = true;
					action = roundActions.getActionsList().get(num).getActionType();
					switch(action){
						case MOVE_STUDENT_TO_ISLAND:
						case STUDENT_FROM_CARD_TO_ISLAND:
							chosenPiece = getColorInput();
							integer = getNumber(" Insert the island ID: ");
							chosenAction = new Action(action, chosenPiece,null, integer);
							break;
						case MOVE_STUDENT_TO_DININGROOM:
						case COLOR_NO_INFLUENCE:
						case STUDENT_FROM_CARD_TO_DINING:
						case STUDENT_FROM_DINING_TO_BAG:
							chosenPiece = getColorInput();
							chosenAction = new Action(action, chosenPiece,null,0);
							break;
						case STUDENT_FROM_ENTRANCE_TO_DINING:
							if(wantToContinue()){
								System.out.println(" Insert the student to remove from entrance");
								chosenPiece = getColorInput();
								System.out.println(" Insert the student to add to dining");
								secondPiece = getColorInput();
								chosenAction = new Action(action, chosenPiece,secondPiece,0);
							}else
								chosenAction = new Action(action, null,null,-1);
							break;
						case STUDENT_FROM_CARD_TO_ENTRANCE:
							if(wantToContinue()) {
								chosenPiece = getColorInput();
								chosenAction = new Action(action, chosenPiece, null, 0);
							}else
								chosenAction = new Action(action, null,null,-1);
							break;
						case MOVE_MOTHER_NATURE:
						case DOUBLE_INFLUENCE:
							integer = getNumber(" Insert the number of mother nature steps: ");
							chosenAction = new Action(action,null,null,integer);
							break;
						case CHOOSE_CLOUD:
							integer = getNumber(" Insert the cloud ID: ");
							chosenAction = new Action(action,null,null,integer);
							break;
						case CHOOSE_CHARACTER:
							integer = getNumber(" Insert the character ID to activate: ");
							chosenAction = new Action(action,null,null,integer);
							break;
						case NO_INFLUENCE:
							integer = getNumber(" Insert the island ID: ");
							chosenAction = new Action(action,null,null,integer);
							break;
					}
				} else {
					showErrorMessage("Invalid action. Try again.");
				}
			} catch (NumberFormatException e) {
				showErrorMessage("Invalid action. Try again.");
			}
		} while (!correct);
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

	private Piece getColorInput() {
		Piece piece;
		while(true) {
			System.out.print(" Insert the student's color [red / blue / green / yellow / purple]: ");
			piece = Piece.getPieceByColor(scanner.nextLine());
			if(piece != null)
				return piece;
			else
				showErrorMessage("Please insert a valid value for color");
		}
	}

	private int getNumber(String request) {
		int num;
		while (true) {
			System.out.print(request);
			String stringRead = scanner.nextLine();
			try {
				num = Integer.parseInt(stringRead);
				return num;
			} catch (NumberFormatException e) {
				showErrorMessage("Please insert a valid number");
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
			System.out.print(" Towers: "+ p.getNumOfTower());
			System.out.print(" Coins: " + p.getCoin());
			if (p.getLastCardUsed() != null)
				System.out.print(" Played card: movements " + p.getLastCardUsed().getCardValue() + " - mother " + p.getLastCardUsed().getMovements());
			System.out.println();
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
				System.out.print("Tower owner: " + island.getIslandOwner().getNickname());
			if(island.getID() == mother)
				System.out.print(" Mother nature");
			System.out.println();
		}

		System.out.println((" ################### CLOUDS #################"));
		for (Cloud cloud : game.getClouds()) {
			System.out.print(" ID: " + cloud.getCloudID());
			System.out.print(" Students: ");
			for (Piece piece:cloud.getStudents()) {
				System.out.print(piece.toString() + " ");
			}
			System.out.println();
		}
		System.out.println("@@@@@@@@@@@ CHARACTERS @@@@@@@@@@@@");
		for (Character character : game.getCharacters()) {
			System.out.print(" ID: " + character.getID() + " cost: " + character.getCost());
			if (character.getID() == 5)
				System.out.print(" no entry: " + character.getIslandFlag());
			if (character.getID() == 1 || character.getID() == 7 || character.getID() == 11) {
				System.out.print(" students: ");
				for (Piece piece : character.getStudents()) {
					System.out.print(piece.toString() + " ");
				}
			}
			System.out.println();
		}
		if (Objects.equals(game.getPlayerHandler().getCurrentPlayer().getId(), playerId))
			System.out.println(" --> It is your turn");
		else
			System.out.println(" --> It is " + game.getPlayerHandler().getCurrentPlayer().getNickname() +"'s turn");
		try {
			System.in.read(new byte[System.in.available()]);
		} catch (IOException ignored) {
		}
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
		System.out.println(" Match finished. The winner is: " + winnerNickname);
	}

	@Override
	public void showErrorMessage(String errorMessage) {
		System.out.println(" Error: "+ errorMessage);
	}
}
