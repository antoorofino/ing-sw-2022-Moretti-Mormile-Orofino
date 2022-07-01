package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.util.AnsiBackColor;
import it.polimi.ingsw.client.cli.util.AnsiColor;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.exception.PlayerException;

import java.util.*;

/**
 * Creates a matrix of characters that contains the full game
 */
public class CLIGame extends CLIMatrix{

	/**
	 * Constructor: builds game's board
	 * @param game the game model received from the server
	 * @param possibleCards the assistant cards that can be played by the player
	 * @param playerId the player id
	 */
	public CLIGame(GameModel game, List<AssistantCard> possibleCards,String playerId,boolean lastRound) {
		super(195,34, AnsiColor.ANSI_DEFAULT, AnsiBackColor.ANSI_DEFAULT);

		boolean first = true;
		// draw board
		for (Player p : game.getPlayerHandler().getPlayers()) {
			CLIMatrix cliPlayer = CLIPlayer(p, game.getTeacherHandler(),p.getId().equals(playerId));

			if (p.getId().equals(playerId))
				drawElement(9, 110, cliPlayer);
			else {
				if (first) {
					drawElement(0, 155, cliPlayer);
					first = false;
				} else
					drawElement(15, 155, cliPlayer);
			}
		}

		// draw islands
		drawText("Islands:",1,1,1);
		CLIMatrix islands = CLIIslandBoard(game.getIslandHandler());
		drawElement(0,0,islands);

		// draw clouds
		CLIMatrix cliCloud;
		drawText("Clouds:",1,10,35);
		for (Cloud cloud : game.getClouds()) {
			cliCloud = CLICloud(cloud);
			drawElement(11,28 + (4-game.getClouds().size())*16*(cloud.getCloudID()-1),cliCloud);
		}

		CLIMatrix cliAssistanceCard;
		List<AssistantCard> deck;
		List<AssistantCard> fullDeck = AssistantCard.createDeck();
		try{
			deck = game.getPlayerHandler().getPlayerById(playerId).getDeck();
		}catch (PlayerException e) {
			deck = new ArrayList<>();
		}
		int i = 10;
		// draw assistant card
		drawText("Your assistance cards:",1,26,5);
		for(AssistantCard card: fullDeck){
			cliAssistanceCard = CLIAssistantCard(card, possibleCards == null || possibleCards.stream().filter(c -> c.getCardID() == card.getCardID()).findFirst().orElse(null) != null, deck.stream().filter(c -> c.getCardID() == card.getCardID()).findFirst().orElse(null) != null);
			drawElement(27,i,cliAssistanceCard);
			i+=12;
		}

		CLIMatrix cliCharacterCard;
		i = 110;
		if(game.getGameMode() == GameMode.EXPERT){
			// draw character card
			drawText("Characters:",1,1,110);
			for (Character character : game.getCharacters()) {
				cliCharacterCard = CLICharacterCard(character);
				drawElement(2,i,cliCharacterCard);
				i+=12;
			}
		}
		// draw turn
		if (Objects.equals(game.getPlayerHandler().getCurrentPlayer().getId(), playerId))
			drawText(" -> It is your turn",1,33,0);
		else
			drawText(" -> It is " + game.getPlayerHandler().getCurrentPlayer().getNickname() +"'s turn",1,33,0);
		// draw last round
		if(lastRound)
			drawText(" -> Last round! <-",1,25,115);
	}

	/**
	 * Draws the main title
	 * @param color color of the title
	 * @param backColor backcolor of the title
	 */
	public static void drawTitle(AnsiColor color, AnsiBackColor backColor) {
		CLIMatrix matrix = new CLIMatrix(200, 18, color, backColor);
		int x = 60;
		int y = 8;
		matrix.drawText(" ▄████████▄   ▄████████▄   ▄█    ▄████████  ███▄▄▄▄       ███     ▄██   ▄      ▄████████",1,y,x);
		matrix.drawText(" ███    ███   ███    ███   ███   ███    ███ ███▀▀▀██▄ ▀█████████▄ ███   ██▄   ███    ███",1,y+1,x);
		matrix.drawText(" ███    █▀    ███    ███   ███   ███    ███ ███   ███    ▀███▀▀██ ███▄▄▄███   ███    █▀ ",1,y+2,x);
		matrix.drawText("▄███▄▄▄      ▄███▄▄▄▄██▀   ███   ███    ███ ███   ███     ███   ▀ ▀▀▀▀▀▀███   ███      ",1,y+3,x);
		matrix.drawText("▀▀███▀▀▀     ▀██████████▄  ███ ▀███████████ ███   ███     ███     ▄██   ███   ▀█████████ ",1,y+4,x);
		matrix.drawText("  ███    █▄   ███     ███  ███   ███    ███ ███   ███     ███     ███   ███          ███ ",1,y+5,x);
		matrix.drawText("  ███    ███  ███     ███  ███   ███    ███ ███   ███     ███     ███   ███    ▄█    ███ ",1,y+6,x);
		matrix.drawText("  ██████████  ███     █▀    ▀█   ███    █▀   ▀█   █▀    ▄█████▀    ▀█████▀   ▄████████▀  ",1,y+7,x);
		matrix.display();
	}

	/**
	 * Draws info about the player
	 * @param player the player
	 * @param teachersHandler the teachers' owner
	 * @param mainPlayer true if is the main player
	 * @return matrix containing the player's table
	 */
	private CLIMatrix CLIPlayer(Player player, TeachersHandler teachersHandler, boolean mainPlayer) {
		CLIMatrix cliPlayer = new CLIMatrix(40, 14, AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		CLIBoard cliBoard = new CLIBoard(player.getId(),mainPlayer? 40 : 35);
		cliBoard.addPieceEntrance(player.getPlayerBoard().getStudentsEntrance());
		cliBoard.drawDiningRoom(player.getPlayerBoard().getStudentsRoom());
		cliBoard.drawTeachers(teachersHandler.getTeachersByPlayerId(player.getId()));
		cliPlayer.drawElement(1,0,cliBoard);
		cliPlayer.drawText("Name : " + player.getNickname(),1,0,0);
		cliPlayer.drawText("Towers: "+ player.getNumOfTowers() + " Coins: " + player.getCoin(),1,12,0);
		if (player.getLastCardUsed() != null)
			cliPlayer.drawText("Played card: value " + player.getLastCardUsed().getCardValue() + " - movements " + player.getLastCardUsed().getMovements(),1,13,0);
		return cliPlayer;
	}

	/**
	 * Draws the player's assistant card
	 * @param card the card to draw
	 * @param playable true if player can use it
	 * @return matrix containing the player's card
	 */
	private CLIMatrix CLIAssistantCard(AssistantCard card, boolean playable,boolean available){
		AnsiColor color = (!available) ? AnsiColor.ANSI_BRIGHT_BLACK : ((!playable) ? AnsiColor.ANSI_RED : AnsiColor.ANSI_YELLOW );
		CLIMatrix cliAssistantCard = new CLIMatrix(11, 6,color,AnsiBackColor.ANSI_DEFAULT);
		cliAssistantCard.drawBorder("╭╮─╰╯│");
		assistanceIcon(cliAssistantCard,card.getCardValue());
		cliAssistantCard.drawText(String.valueOf(card.getCardValue()),1,1,1);
		cliAssistantCard.drawText(String.valueOf(card.getMovements()),1,1,9);
		return cliAssistantCard;
	}

	/**
	 * Creates a random icon for assistant card
	 * @param assistance the card to draw
	 */
	private void assistanceIcon(CLIMatrix assistance,int index){
		assistance.drawText(" ___ ",1,1,3);
		List<String> faces = Arrays.asList("J'-'L","%\"-\"%","|'-'|","/*_*\\");
		assistance.drawText(faces.get(index % 4), 1,2,3);
		List<String> bodies = Arrays.asList("-[:]-","/[U]\\","/[\\]\\","/[Y]\\");
		assistance.drawText(bodies.get(index % 4), 1,3,3);
		List<String> feet = Arrays.asList("_/ |_","_| \\_","_/ \\_","_| |_");
		assistance.drawText(feet.get(index % 4),1,4,3);
	}

	/**
	 * Draws the game's cloud
	 * @param cloud the cloud to draw
	 * @return matrix containing the game's cloud
	 */
	private CLIMatrix CLICloud(Cloud cloud) {
		CLIMatrix cliCloud = new CLIMatrix(15, 4, AnsiColor.ANSI_DEFAULT, AnsiBackColor.ANSI_DEFAULT);
		cliCloud.drawText("╭─────╮", 1, 0, 3);
		cliCloud.drawText("╭─╯     ╰───╮", 1, 1, 1);
		cliCloud.drawText("╭╯           ╰╮", 1, 2, 0);
		cliCloud.drawText("╰─────────────╯", 1, 3, 0);
		cliCloud.drawText(String.valueOf(cloud.getCloudID()), 1, 0, 11);
		int column = 3;
		for (Piece p:cloud.getStudents()) {
			cliCloud.elements[2][column].color = AnsiColor.getAnsiByPiece(p);
			cliCloud.elements[2][column].symbol = '●';
			if(cloud.getStudents().size() != 3 )
				column+= 2;
			else
				column+= 3;
		}
		return cliCloud;
	}

	/**
	 * Draws the game's character card
	 * @param character the card to draw
	 * @return matrix containing the character card
	 */
	private CLIMatrix CLICharacterCard(Character character) {
		CLIMatrix cliCharacterCard = new CLIMatrix(10, 6, AnsiColor.ANSI_DEFAULT, AnsiBackColor.ANSI_DEFAULT);
		cliCharacterCard.drawBorder("╭╮─╰╯│");
		if(character.getID() == 5) {
			cliCharacterCard.drawText("! x" + character.getIslandFlag(), 1, 3, 3);
			cliCharacterCard.elements[3][3].color = AnsiColor.ANSI_RED;
		}
		cliCharacterCard.drawText(String.valueOf(character.getID()),1,1,1);
		cliCharacterCard.drawText("©:" + character.getCost(),1,1,6);
		cliCharacterCard.elements[1][6].color = AnsiColor.ANSI_YELLOW;
		// draw students
		int column = 3;
		int row = 2;
		for (Piece p:character.getStudents()) {
			cliCharacterCard.elements[row][column].color = AnsiColor.getAnsiByPiece(p);
			cliCharacterCard.elements[row][column].symbol = '●';
			if(column!=6)
				column+= 3;
			else{
				column = 3;
				row+= 1;
			}
		}
		return cliCharacterCard;
	}

	/**
	 * Draws the set of islands
	 * @param islandsHandler the island handler
	 * @return matrix containing the set of islands
	 */
	private CLIMatrix CLIIslandBoard(IslandsHandler islandsHandler) {
		CLIMatrix cliIslandBoard = new CLIMatrix(105, 26,AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		ArrayList<Island> islands = islandsHandler.getIslands();
		int index = (12 - islandsHandler.getCountsLastMerge())%12; // countsLastMerge
		final int[][] absolutePositions = { { 0, 14 }, { 0, 34 }, { 0, 54 }, { 0, 74 }, { 5, 88 }, { 13, 88 }, { 18, 74 }, { 18, 54 }, { 18, 34 }, { 18, 14 }, { 13, 0 }, { 5, 0 } };
		final int[][] relativePosition = { { -3, 12 }, { 0, 16 }, { 0, 16 }, { 0, 16 }, { 3, 12 }, { 6, 0 }, { 3, -12 }, { 0, -16 }, { 0, -16 }, { 0, -16 }, { -3, -12 }, { -6, 0 }};
		int x = 0;
		int y = 0;
		boolean mother;
		CLIIsland cliIsland;
		for(Island island:islands){
			mother = island.getID() == islandsHandler.getMotherNature();
			for(int i = 0;i < island.getSize(); i++){
				cliIsland = new CLIIsland(mother, island.getID());
				if(island.getIslandOwner()!=null)
					cliIsland.drawOwner(island.getIslandOwner().getNickname());
				if(island.getFlagNoInfluence()!=0)
					cliIsland.drawNoEntry(island.getFlagNoInfluence());
				if(island.getSize()==1 || i==1)
					cliIsland.addStudents(island.getStudentsOnIsland());
				if(i!=0){
					y += relativePosition[index][0];
					x += relativePosition[index][1];
				}else{
					y = absolutePositions[index][0];
					x = absolutePositions[index][1];
				}
				cliIslandBoard.drawElement(y,x,cliIsland);
				index = (index + 1)%12;
			}
		}
		return cliIslandBoard;
	}
}
