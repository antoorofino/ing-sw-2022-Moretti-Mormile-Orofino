package it.polimi.ingsw.util;

import it.polimi.ingsw.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameListInfo implements Serializable {
	private final String gameName;
	private final GameMode gameMode;
	private final int numPlayers;

	public GameListInfo(String gameName, GameMode gameMode, int numPlayers){
		this.gameName = gameName;
		this.gameMode = gameMode;
		this.numPlayers = numPlayers;
	}

	public String getGameName(){
		return gameName;
	}

	public GameMode getGameMode(){
		return gameMode;
	}

	public int getNumPlayers(){
		return numPlayers;
	}

	private static GameListInfo getGameInfo(GameModel gameModel){
		return new GameListInfo(gameModel.getGameName(),
				gameModel.getGameMode(),
				gameModel.getPlayerHandler().getNumPlayers() - gameModel.getPlayerHandler().getPlayers().size()
		);
	}

	public static List<GameListInfo> createGameInfoList(List<GameModel> gameList){
		ArrayList<GameListInfo> list = new ArrayList<>();
		for (GameModel game : gameList){
			list.add(GameListInfo.getGameInfo(game));
		}
		return list;
	}
}
