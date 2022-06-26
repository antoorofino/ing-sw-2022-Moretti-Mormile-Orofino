package it.polimi.ingsw.util;

import it.polimi.ingsw.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores information about a single match
 */
public class GameListInfo implements Serializable {
	private final String gameName;
	private final GameMode gameMode;
	private final int numPlayers;

	/**
	 * Constructor: build the information game store
	 * @param gameName name of the game
	 * @param gameMode game mode
	 * @param numPlayers number of players
	 */
	public GameListInfo(String gameName, GameMode gameMode, int numPlayers){
		this.gameName = gameName;
		this.gameMode = gameMode;
		this.numPlayers = numPlayers;
	}

	/**
	 * Get the game's name
	 * @return game's name
	 */
	public String getGameName(){
		return gameName;
	}

	/**
	 * Get game's mode
	 * @return games's mode
	 */
	public GameMode getGameMode(){
		return gameMode;
	}

	/**
	 * Get number of player in the match
	 * @return number of player in the match
	 */
	public int getNumPlayers(){
		return numPlayers;
	}

	/**
	 * Get the information of all the matches created
	 * @param gameModel game model
	 * @return list of all matches created
	 */
	private static GameListInfo getGameInfo(GameModel gameModel){
		return new GameListInfo(gameModel.getGameName(),
				gameModel.getGameMode(),
				gameModel.getPlayerHandler().getNumPlayers()
		);
	}

	/**
	 * Create a list of information for all matches created
	 * @param gameList list of game created
	 * @return the information about a game for all matches created
	 */
	public static List<GameListInfo> createGameInfoList(List<GameModel> gameList){
		ArrayList<GameListInfo> list = new ArrayList<>();
		for (GameModel game : gameList){
			list.add(GameListInfo.getGameInfo(game));
		}
		return list;
	}
}
