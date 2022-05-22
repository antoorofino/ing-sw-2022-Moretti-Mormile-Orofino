package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.util.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CLIView implements View{
	private final Scanner scanner;
	// TODO: se qualcuno scrive mentre non è il tuo turno si riempe buffer, chiedi a pale
	private Thread inputOutOfTurn;
	private boolean isYourTurn;
	private AtomicBoolean myTurn = new AtomicBoolean(false);
	private ServerHandler serverHandler;
	private String playerId;
	private String nickname;

	/* cose da chiedere ad anto ( che se ha voglia puo aggiungere le chiamate dei messaggi CV al server )
	in caso di setnickname (e settowercolor) aspetto ack (che non mi convince.. perchè non posso essere a posto e al limite mi rimandi un'altra ask?)
	ack deve essere un messaggio di tipo CV (non l'ho creato), al contrario di quanto scritto sul pdf SYS
	va serializzato game model in game info
	manca settower color nel controller
	dopo che sono entrato nel gioco e ti ho dato nick, invece dell'ack esplicito potresti invocare con un messaggio il mio metodo:

	**
	 * Shows a message to say to the user that is connected to
	 * the server and are waiting other user
	 *
	void showQueuedMessage();

	oppure rispondermi con il messaggio gamestart se sono l'ultimo a entrare
	 */

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
		boolean correct = false;
		while(!correct){
			correct = true;
			System.out.println(" Do do you want to create a new game or join an existing one? [n/e]");
			String preferredMode = scanner.nextLine();
			if ((preferredMode.equalsIgnoreCase("n")))
				serverHandler.send(new NewGameMessage(playerId));
			else if ((preferredMode.equalsIgnoreCase("e")))
				serverHandler.send(new AskGameListMessage(playerId));
			else{
				System.out.println(" Please insert a correct value");
				correct = false;
			}
		}
	}

	@Override
	public void askGameSettings() {
		boolean correct = false;
		int numPlayers = 0;
		GameMode gameMode = GameMode.BASIC;
		while(!correct){
			System.out.print("> Enter number of player: ");
			numPlayers = Integer.parseInt(scanner.nextLine());
			if(InputValidator.isNumberBetween(numPlayers,1,4))
				correct = true;
			if (!correct) {
				System.out.println(" > Invalid number. Try again.");
			}
		}
		correct = false;
		while(!correct){
			System.out.print("> Basic mode? [y/n]: ");
			String preferredMode = scanner.nextLine();
			if ((preferredMode.equalsIgnoreCase("y")))
				gameMode = GameMode.BASIC;
			else
				gameMode = GameMode.EXPERT;
		}
		serverHandler.send(new SetGameSettings(playerId,gameMode,numPlayers));
	}

	@Override
	public void askNickname() {
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
	public void askTowerColor(ArrayList<String> possibleColor) {
		String chosenColor;
		//There's only one color?
		if (possibleColor.size() == 1) {
			chosenColor = possibleColor.get(0);
			System.out.print(" > Your color will be" + chosenColor);
		} else {
			boolean correct;
			do {
				System.out.print(" > Choose your tower color between: ");
				for (int i = 0; i < possibleColor.size(); i++) {
					String color = possibleColor.get(i);
					System.out.print(color);
				}
				System.out.println();
				System.out.print("  ↳: ");
				chosenColor = scanner.next().toLowerCase();
				correct = InputValidator.isColorBetween(chosenColor,possibleColor);
				if (!correct) {
					System.out.println(" > Invalid choice. Try again.");
				}
			} while (!correct);
		}
		serverHandler.send(new SetTowerColor(playerId,chosenColor));
	}

	@Override
	public void askAssistantCard(ArrayList<AssistantCard> cards) {
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
	public void askAction(RoundActions roundActions) {
		String action;
		Piece chosenPiece;
		int chosenId;
		Action chosenAction = null;
		boolean correct;

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
	public void showGamesList(GamesListInfo gamesList) {
		for (String game:gamesList.getGamesInfo()) {
			System.out.println(game);
		}
	}

	public void showPossibleActions(RoundActions roundActions) {
		System.out.println("Your possible actions are: ");
		for (Action action:roundActions.getActionsList()) {
			System.out.println(action.getActionType().toString() + " / ");
		}
	}


	@Override
	public void showGame(GameInfo gameInfo) {
		// TODO: print the game info
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
