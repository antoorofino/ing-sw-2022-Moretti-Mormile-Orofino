package it.polimi.ingsw.util;


import it.polimi.ingsw.model.AssistantCard;

import java.util.ArrayList;
import java.util.List;

public class InputValidator {
	//TODO create methods input validator
	public static boolean isIp(String serverIP) {
		for(int i=0; i<serverIP.length(); i++){
			if(!((serverIP.charAt(i)<='9' && serverIP.charAt(i)>='0') || serverIP.charAt(i)=='.')){
				return false;
			}
		}
		return true;
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
		if(numAction >= 0 && numAction<roundActions.getActionsList().size())
			return true;
		return false;
	}
}

