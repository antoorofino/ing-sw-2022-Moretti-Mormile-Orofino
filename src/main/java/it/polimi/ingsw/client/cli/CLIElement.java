package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.util.AnsiBackColor;
import it.polimi.ingsw.client.cli.util.AnsiColor;

/**
 * Main element of the character matrix
 */
public class CLIElement {
	char symbol;
	AnsiColor color;
	AnsiBackColor backColor;

	/**
	 * Constructor: build a single empty cell
	 */
	CLIElement(){
		this.symbol = ' ';
		this.color = AnsiColor.ANSI_DEFAULT;
		this.backColor = AnsiBackColor.ANSI_DEFAULT;
	}
}
