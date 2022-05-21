package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.util.GameInfo;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.TowerColor;

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
	 * Asks the number of players and the game mode
	 *
	 */
	void askGameSettings();


	/**
	 * Asks the player's nickname
	 *
	 */
	void askNickname();


	/**
	 * Asks the player color
	 *
	 * @param possibleColor All the possible Color
	 */
	void askTowerColor(ArrayList<String> possibleColor);

	/**
	 * Asks the player card assistance
	 *
	 * @param cards All the possible cards
	 */
	void askAssistantCard(ArrayList<AssistantCard> cards);

	/**
	 * Asks the action the player wants to perform
	 *
	 * @param roundActions  All the possible actions
	 */
	void askAction(RoundActions roundActions);

	/**
	 * Shows the board of the game
	 *
	 * @param gameInfo   The game info
	 */
	void showGame(GameInfo gameInfo);

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
	 */
	void showMessage(String message);

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

	// TODO: add the termination
	/**
	 *
	 * Shows an error message to the user and terminate the client
	 *
	 * @param errorMessage The message to be shown
	 */
	void showErrorMessage(String errorMessage);

}
