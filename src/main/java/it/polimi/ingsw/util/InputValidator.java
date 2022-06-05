package it.polimi.ingsw.util;


import it.polimi.ingsw.model.AssistantCard;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
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

	public static boolean isPortNumber(int port) {
		return InputValidator.isNumberBetween(port, 0,65536);
	}

	public static boolean isPortNumber(String port) {
		int portNumber;
		try {
			portNumber = Integer.parseInt(port);
			return InputValidator.isNumberBetween(portNumber, 0,65536);
		} catch (NumberFormatException ignored) {
			return false;
		}
	}

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

