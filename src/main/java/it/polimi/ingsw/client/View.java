package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.TowerColor;

import java.util.List;

public interface View {

	/**
	 * Sets the serverHandler
	 *
	 * @param serverHandler The serverHandler
	 */
	void setServerHandler(ServerHandler serverHandler);

	/**
	 * Interface launcher. Asks the server IP to connect to and notify it to the serverHandler
	 */
	void run();
	void setPlayerId(String playerId);

	void askNewGameName();

	void askNewGameChoice();

	/**
	 * Asks the player's nickname
	 *
	 */
	void askNickname(boolean isFirstRequest);

	/**
	 * Asks the player color
	 *
	 * @param possibleColor All the possible Color
	 */
	void askTowerColor(List<TowerColor> possibleColor, boolean isFirstRequest);

	/**
	 * Asks the player card assistance
	 *
	 * @param cards All the possible cards
	 */
	void askAssistantCard(List<AssistantCard> cards,GameModel game);

	/**
	 * Asks the action the player wants to perform
	 *
	 * @param roundActions  All the possible actions
	 */
	void askAction(RoundActions roundActions,boolean isInvalidAction);

	void showGamesList(List<GameListInfo> gamesList);

	void showGameStart(GameModel game, String firstPlayerNickname);
	/**
	 * Shows the board of the game
	 *
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
	 * Notify the players that the game has ended and notify the winner
	 *
	 * @param winnerNickname The nickname of the winner
	 */
	void showGameEndMessage(String winnerNickname);

	/**
	 * Shows an error message to the user and terminate the client
	 */
	void showConnectionErrorMessage();

	void showErrorOnConnection();

	void showDisconnection(String playerDisconnected);
}
