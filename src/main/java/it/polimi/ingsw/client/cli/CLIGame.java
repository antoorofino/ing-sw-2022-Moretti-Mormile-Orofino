package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.exception.PlayerException;

import java.util.ArrayList;
import java.util.List;

public class CLIGame extends CLIMatrix{
	public CLIGame(GameModel game, List<AssistantCard> possibleCards,String playerId) {
		super(195,33,AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);

		boolean first = true;
		// draw board
		for (Player p : game.getPlayerHandler().getPlayers()) {
			CLIPlayer cliPlayer = new CLIPlayer(p, game.getTeacherHandler(),p.getId().equals(playerId));

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
		CLIIslandBoard islands = new CLIIslandBoard(game.getIslandHandler());
		drawElement(0,0,islands);

		// draw clouds
		CLICloud cliCloud;
		drawText("Clouds:",1,10,35);
		for (Cloud cloud : game.getClouds()) {
			cliCloud = new CLICloud(cloud);
			drawElement(11,28 + (4-game.getClouds().size())*16*(cloud.getCloudID()-1),cliCloud);
		}

		CLIAssistanceCard cliAssistanceCard;
		List<AssistantCard> deck;
		try{
			deck = game.getPlayerHandler().getPlayerById(playerId).getDeck();
		}catch (PlayerException e) {
			deck = new ArrayList<>();
		}
		int i = 10;
		// draw assistant card
		// FIXME: quello che dicevo ad anto per gli assistenti utilizzati
		drawText("Your assistance cards:",1,26,5);
		for(AssistantCard card: deck){
			cliAssistanceCard = new CLIAssistanceCard(card, possibleCards == null || possibleCards.contains(card));
			drawElement(27,i,cliAssistanceCard);
			i+=12;
		}


		CLICharacterCard cliCharacterCard;
		i = 110;
		if(game.getGameMode() == GameMode.EXPERT){
			// draw character card
			drawText("Characters:",1,1,110);
			for (Character character : game.getCharacters()) {
				cliCharacterCard = new CLICharacterCard(character);
				drawElement(2,i,cliCharacterCard);
				i+=12;
			}
		}

	}


	// creerà la tavola completa ricevendo GameInfo
}
