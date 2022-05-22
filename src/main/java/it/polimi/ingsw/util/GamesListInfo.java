package it.polimi.ingsw.util;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// trasforma lista di game in lista di stringhe TODO: da modificare per la gui
public class GamesListInfo implements Serializable {
	private ArrayList<String> gamesInfo;

	public GamesListInfo(List<GameModel> games){
		gamesInfo = new ArrayList<>();
		for (GameModel game:games){
			String info = "GameMode : " + game.getGameMode() + " Num players: " + game.getPlayerHandler().getNumPlayers() + " Current players:";
			for (Player p: game.getPlayerHandler().getPlayers()) {
				info += " " + p.getId() ;
			}
		}
	}

	public ArrayList<String> getGamesInfo(){
		return gamesInfo;
	}
}
