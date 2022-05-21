package it.polimi.ingsw.util;


import java.util.ArrayList;

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
	 * @param serverIP  The entered server ip
	 * @return  True if the ip is empty, otherwise false
	 */
	public static boolean isEmptyIp(String serverIP) {
		return serverIP.equals("");
	}

	public static boolean isColorBetween(String choice, ArrayList<String> availableColors) {
		for (String color : availableColors) {
			if (choice.equalsIgnoreCase(color)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNickname(String nickname) {
		return !(nickname.equals("") || nickname.contains(" "));
	}

	public static boolean isNumberBetwenn(int num, int a, int b){ return true;}
}

