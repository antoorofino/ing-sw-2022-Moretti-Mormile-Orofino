package it.polimi.ingsw.util;

import it.polimi.ingsw.client.cli.util.AnsiColor;

public class CLIFrmt {

	/**
	 * Returns ansi code based on the specified color char
	 * @param color the color char
	 * @return ansicolor code
	 */
	private static AnsiColor getColor(char color) {
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
	 * Returns ansi code based on the specified style char
	 * @param style the style char
	 * @return ansi style code
	 */
	private static String getStyle(char style) {
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

	/**
	 * Returns a string containing the ansi code of the requested style and color
	 * @param style the selected style
	 * @param color the selected color
	 * @param text the message
	 * @return the formatted string
	 */
	public static String print(char style, char color, String text) {
		return getStyle(style) + getColor(color).getCode() + text + getColor('d').getCode();
	}

	/**
	 * Returns a string containing the ansi code of the requested style and color
	 * @param color the selected color
	 * @param text the message
	 * @return the formatted string
	 */
	public static String print(char color, String text) {
		return getColor(color).getCode() + text;
	}
}
