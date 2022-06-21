package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.util.AnsiBackColor;
import it.polimi.ingsw.client.cli.util.AnsiColor;

public class CLIElement {
	char symbol;
	AnsiColor color;
	AnsiBackColor backColor;

	CLIElement(){
		this.symbol = ' ';
		this.color = AnsiColor.ANSI_DEFAULT;
		this.backColor = AnsiBackColor.ANSI_DEFAULT;
	}
}
