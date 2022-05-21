package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.util.GameInfo;
import it.polimi.ingsw.util.RoundActions;

import java.util.ArrayList;

public interface View {
    //TODO: check if some else needed
	/**
	 * Sets the serverHandler
	 *
	 * @param serverHandler The serverHandler
	 */
	void setServerHandler(ServerHandler serverHandler);

	/**
	 * Interface launcher. Asks the server IP to connect to and notify it to the serverHandler
	 */
	void launch();

	void setPlayerId(String playerId);

	/**
	 * Asks a new nickname to the user and notify the choice to the serverHandler
	 */
	void askNewNickname();

	/**
	 * Asks nickname and if it's a new game the number of players
	 * for the game and notify the information to the serverHandler
	 *
	 * @param newGame True if the it is a new game, otherwise false
	 */
	void askGameSettings(boolean newGame);

	/**
	 * Asks the player color
	 *
	 * @param possibleColor All the possible Color
	 */
	void askPlayerColor(ArrayList<String> possibleColor);

	/**
	 * Asks the player card assistance
	 *
	 * @param cards All the possible cards
	 */
	void askAssistantCard(ArrayList<AssistantCard> cards);

	/**
	 * Asks the player a new card assistance
	 *
	 * @param cards All the possible cards
	 */
	void askNewAssistantCard(ArrayList<AssistantCard> cards);

	/**
	 * Asks the action the player wants to perform
	 *
	 * @param roundActions  All the possible actions
	 * @param gameInfo       The game info
	 */
	void askAction(RoundActions roundActions, GameInfo gameInfo);

	/**
	 * Shows the possible action that the player can do
	 * @param roundActions The possible actions
	 */

	void showPossibleActions(RoundActions roundActions);

	/**
	 * Shows the board of the game
	 *
	 * @param gameInfo   The game info
	 * @param newScreen True if it's necessary to clean the interface
	 */
	void showGame(GameInfo gameInfo, boolean newScreen);

	/**
	 * Shows the user who is taking his turn
	 *
	 * @param currentNickname The nickname of the user who is taking his turn
	 */
	void showTurn(String currentNickname);

	/**
	 * Shows a specified message to the user
	 *
	 * @param message   The message to be shown
	 * @param newScreen True if it's necessary to clean the interface
	 */
	void showMessage(String message, boolean newScreen);

	/**
	 * Shows a message to say to the user that is connected to
	 * the server and will be added to the next available game
	 */
	void showQueuedMessage();

	/**
	 * Notify the players that the game has ended and notify the winner
	 *
	 * @param winnerNickname The nickname of the winner
	 * @param youWin         True if the player has win
	 */
	void showGameEndMessage(String winnerNickname, boolean youWin);

	/**
	 * Shows an error message to the user
	 *
	 * @param errorMessage The message to be shown
	 * @param newScreen    True if it's necessary to clean the interface
	 */
	void showErrorMessage(String errorMessage, boolean newScreen);

}
