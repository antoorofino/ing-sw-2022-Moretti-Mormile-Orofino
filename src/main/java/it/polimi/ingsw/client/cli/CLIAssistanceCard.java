package it.polimi.ingsw.client.cli;

public class CLIAssistanceCard extends CLIMatrix {

	public CLIAssistanceCard(int value,int mov,boolean playable) {
		super(10, 5, playable ? AnsiColor.ANSI_GREEN : AnsiColor.ANSI_DEFAULT);
		drawBorder("╒╕═╘╛│");
		drawText("Val: " + value,1,1,1);
		drawText("Mov: "+ mov,1,height-2,3);
	}
}
