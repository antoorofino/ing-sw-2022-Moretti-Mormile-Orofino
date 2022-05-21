package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
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
	ack deve essere un messaggio di tipo CV, al contrario di quanto scritto sul pdf SYS
	showDisconnection è stato trasformato in CV, al contrario del pdf
	va serializzato game model in game info e in un elenco per la game list
	manca settower color nel controller
	il messaggio vc per come hai scritto il server ha il metodo execute con il controller e non virtualview -> gia cambiati i messaggi (e il parametro in clienthandler) ma ora mi genera eccezione perchè non esiste il controller
	dopo che sono entrato nel gioco e ti ho dato nick, invece dell'ack esplicito potresti invocare con un messaggio il mio metodo:

	**
	 * Shows a message to say to the user that is connected to
	 * the server and are waiting other user
	 *
	void showQueuedMessage();

	oppure rispondermi con il messaggio gamestart se sono l'ultimo a entrare

	in setAssistantCard ti passo il playerid coma da pdf ma te mi cerchi per nickname, ti passo quello (ho sia il mio id che il mio nickname in view)
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
			// TODO: InputValidator.isBetween
			if((numPlayers>1)&&(numPlayers<4))
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
				//correct = inputValidator.isIDbetween(chosenID,possibleCards);
				if (!correct) {
					System.out.println(" > Invalid choice. Try again.");
				}
			} while (!correct);
		}
		// FIXME: get AssistantCard by ID
		serverHandler.send(new SetAssistantCard(playerId,cards.get(0)));
	}

	@Override
	public void askAction(RoundActions roundActions) {
		//TODO: create the action menu
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
