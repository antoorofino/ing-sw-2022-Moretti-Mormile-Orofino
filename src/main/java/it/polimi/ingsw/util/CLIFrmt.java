package it.polimi.ingsw.util;

import it.polimi.ingsw.client.cli.AnsiColor;

public class CLIFrmt {
	/**
	 *
	 * @param color
	 * @return ansicolor code
	 */
	protected static AnsiColor getColor(char color) {
		color = Character.toLowerCase(color);
		switch (color) {
			case 'r':
				return AnsiColor.ANSI_RED;
			case 'b':
				return AnsiColor.ANSI_BLUE;
			case 'g':
				return AnsiColor.ANSI_GREEN;
			case 'y':
				return AnsiColor.ANSI_YELLOW;
			case 'p':
				return AnsiColor.ANSI_PURPLE;
			default:
				return AnsiColor.ANSI_DEFAULT;
		}
	}

	/**
	 *
	 * @param style
	 * @return clifrmt code
	 */
	public static String getStyle(char style) {
		style = Character.toLowerCase(style);
		switch (style) {
			case 'i':
				return "\u001B[3m";
			case 'b':
				return "\u001b[1m";
			case 'r':
				return "\u001b[7m";
			case 'u':
				return "\u001b[4m";
			default:
				return "\u001B[0m";
		}
	}

	public static String println(char style,char color, String text) {
		return getStyle(style) + getColor(color).getCode() + text;
	}

	public static String println(char color, String text) {
		return getColor(color).getCode() + text;
	}
}
