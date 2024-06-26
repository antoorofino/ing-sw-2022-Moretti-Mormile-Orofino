package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.TowerColor;

import java.util.List;

/**
 * View interface with methods invoked by messages
 */
public interface View {

	/**
	 * Sets the serverHandler
	 * @param serverHandler The serverHandler
	 */
	void setServerHandler(ServerHandler serverHandler);

	/**
	 * Interface launcher. Asks the server IP to connect to and notify it to the serverHandler
	 */
	void run();

	/**
	 * Sets in the view the id generated by the controller
	 * @param playerId the generated id
	 */
	void setPlayerId(String playerId);

	/**
	 * Asks for a new game name in case the one inserted before is already in use
	 */
	void askNewGameName();

	/**
	 * Asks for the name of the game it wants to join in case the one selected is full
	 */
	void askNewGameChoice();

	/**
	 * Asks the player's nickname
	 * @param isFirstRequest true if is the first nickname request
	 */
	void askNickname(boolean isFirstRequest);

	/**
	 * Asks the player color
	 * @param possibleColors All the possible Color
	 */
	void askTowerColor(List<TowerColor> possibleColors, boolean isFirstRequest);

	/**
	 * Asks the player card assistance
	 * @param cards All the possible cards
	 */
	void askAssistantCard(List<AssistantCard> cards,GameModel game);

	/**
	 * Asks the action the player wants to perform
	 * @param roundActions  All the possible actions
	 */
	void askAction(RoundActions roundActions,boolean isInvalidAction);

	/**
	 * Shows to user the list of possible game to join
	 * @param gamesList the possible games
	 */
	void showGamesList(List<GameListInfo> gamesList);

	/**
	 * Shows message about the start of the game
	 * @param game the game model
	 * @param firstPlayerNickname the nickname of first player to play
	 */
	void showGameStart(GameModel game, String firstPlayerNickname);

	/**
	 * Shows the board of the game
	 */
	void showGame(GameModel game);

	/**
	 * Shows a specified message to the user
	 */
	void showLastRound();

	/**
	 * Shows a message to say to the user that is connected to
	 * the server and will be added to the next available game
	 */
	void showQueuedMessage();

	/**
	 * Notifies the players that the game has ended and notify the winner
	 * @param winnerNickname The nickname of the winner
	 */
	void showGameEndMessage(String winnerNickname);

	/**
	 * Shows an error message to the user and terminate the client
	 */
	void showConnectionErrorMessage();

	/**
	 * Shows an error message to the user when unable to connect to the server
	 */
	void showErrorOnConnection();

	/**
	 * Shows an error message to the user when someone disconnects
	 * @param playerDisconnected the name of the disconnected player
	 */
	void showDisconnection(String playerDisconnected);
}
