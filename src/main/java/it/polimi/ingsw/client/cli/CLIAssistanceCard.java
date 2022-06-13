package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.AssistantCard;

public class CLIAssistanceCard extends CLIMatrix {

	public CLIAssistanceCard(AssistantCard card, boolean playable) {
		super(9, 6, playable ? AnsiColor.ANSI_DEFAULT : AnsiColor.ANSI_RED,AnsiBackColor.ANSI_DEFAULT);
		drawBorder("╭╮─╰╯│");
		drawText("   ○ ",1,2,1);
		drawText(" ( | )",1,3,1);
		drawText("  / \\",1,4,1);
		drawText(String.valueOf(card.getCardValue()),1,1,1);
		drawText(String.valueOf(card.getMovements()),1,1,7);
		/*
         1  ○  2
		  ( | )
           / \
		*/
	}
}
