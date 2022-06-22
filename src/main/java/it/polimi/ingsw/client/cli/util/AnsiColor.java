package it.polimi.ingsw.client.cli.util;

import it.polimi.ingsw.model.Piece;

/**
 * Enum class for ansi color codes
 */
public enum AnsiColor {
	ANSI_BLACK("\u001B[30m"),
	ANSI_RED  ("\u001B[31m"),
	ANSI_GREEN("\u001B[32m"),
	ANSI_YELLOW("\u001B[33m"),
	ANSI_BLUE ("\u001B[34m"),
	ANSI_PURPLE("\u001B[35m"),
	ANSI_CYAN ("\u001B[36m"),
	ANSI_WHITE("\u001B[37m"),
	ANSI_BRIGHT_BLACK("\u001B[90m"),
	ANSI_BRIGHT_RED  ("\u001B[91m"),
	ANSI_BRIGHT_GREEN("\u001B[92m"),
	ANSI_BRIGHT_YELLOW("\u001B[93m"),
	ANSI_BRIGHT_BLUE ("\u001B[94m"),
	ANSI_BRIGHT_PURPLE("\u001B[95m"),
	ANSI_BRIGHT_CYAN ("\u001B[96m"),
	ANSI_BRIGHT_WHITE("\u001B[97m"),
	ANSI_DEFAULT("\u001B[0m");

	private final String code;

	/**
	 * Constructor: build ansi code
	 * @param code ansi code
	 */
	AnsiColor(String code){
		this.code = code;
	}

	/**
	 * Get ansi color from piece
	 * @param p the piece
	 * @return the color code of the piece
	 */
	public static AnsiColor getAnsiByPiece(Piece p){
		switch(p){
			case UNICORN:
				return AnsiColor.ANSI_BLUE;
			case DRAGON:
				return AnsiColor.ANSI_RED;
			case GNOME :
				return AnsiColor.ANSI_YELLOW;
			case FROG:
				return AnsiColor.ANSI_GREEN;
			case FAIRY:
				return AnsiColor.ANSI_BRIGHT_PURPLE;
		}
		return null;
	}

	/**
	 * Get ansi code from enum
	 * @return the ansi code
	 */
	public String getCode(){
		return code;
	}
}
