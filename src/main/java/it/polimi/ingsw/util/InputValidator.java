package it.polimi.ingsw.util;


import it.polimi.ingsw.model.AssistantCard;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks input string
 */
public class InputValidator {
	/**
	 * Checks if the entered string is an ip
	 * @param serverIP input string
	 * @return true if string is an ip, otherwise false
	 */
	public static boolean isIp(String serverIP) {
		String zeroTo255
				= "(\\d{1,2}|(0|1)\\"
				+ "d{2}|2[0-4]\\d|25[0-5])";
		String regex
				= zeroTo255 + "\\."
				+ zeroTo255 + "\\."
				+ zeroTo255 + "\\."
				+ zeroTo255;
		if (serverIP == null) {
			return false;
		}
		return Pattern.compile(regex).matcher(serverIP).matches();
	}

	/**
	 * Tests if the entered ip is empty
	 * @param string  The entered server ip
	 * @return  True if the ip is empty, otherwise false
	 */
	public static boolean isEmpty(String string) {
		return string.equals("");
	}

	/**
	 * Tests if the entered ip is a port
	 * @param port  The entered server port
	 * @return  True if the string is a port, otherwise false
	 */

	public static boolean isPortNumber(String port) {
		if(!isNumber(port))
			return false;
		return InputValidator.isNumberBetween(Integer.parseInt(port), 0,65536);
	}

	/**
	 * Tests if the entered string is a number
	 * @param number The entered string
	 * @return true if the string is a number, otherwise false
	 */
	public static boolean isNumber(String number) {
		int Number;
		try {
			Number = Integer.parseInt(number);
			return true;
		} catch (NumberFormatException ignored) {
			return false;
		}
	}

	/**
	 * Checks if user chose an available color
	 * @param choice color that user chose
	 * @param availableColors list of available colors
	 * @return true if chose an available color, otherwise false
	 */
	public static boolean isTowerColorBetween(TowerColor choice, List<TowerColor> availableColors) {
		for (TowerColor color : availableColors) {
			if (choice == color) {
				return true;
			}
		}
		return false;
	}

	public static boolean isWordNotEmpty(String nickname) {
		return !(nickname.equals("") || nickname.contains(" "));
	}

	public static boolean isNumberBetween(int num, int a, int b){ return ( a <= num ) && ( num <= b );}

	public static AssistantCard isIDBetween(int id, List<AssistantCard> cards){
		for (AssistantCard card:cards) {
			if(card.getCardID() == id)
				return card;
		}
		return null;
	}

	public static boolean isGameName(String gameName, List<GameListInfo> gamesList){
		for (GameListInfo game:gamesList) {
			if(game.getGameName().equalsIgnoreCase(gameName))
				return true;
		}
		return false;
	}

	public static boolean isValidAction(int numAction,RoundActions roundActions){
		return numAction >= 0 && numAction < roundActions.getActionsList().size();
	}
}

