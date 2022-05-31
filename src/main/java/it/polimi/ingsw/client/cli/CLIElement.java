package it.polimi.ingsw.client.cli;

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
