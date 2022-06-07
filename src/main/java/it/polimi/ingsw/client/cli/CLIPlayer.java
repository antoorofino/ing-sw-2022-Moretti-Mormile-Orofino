package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TeachersHandler;

public class CLIPlayer extends CLIMatrix{
	public CLIPlayer(Player player, TeachersHandler teachersHandler) {
		super(40, 15, AnsiColor.ANSI_DEFAULT);
		CLIBoard cliBoard = new CLIBoard(player.getId());
		cliBoard.addPieceEntrance(player.getPlayerBoard().getStudentsEntrance());
		cliBoard.drawDiningRoom(player.getPlayerBoard().getStudentsRoom());
		cliBoard.drawTeachers(teachersHandler.getTeachers());
		drawElement(1,0,cliBoard);

		drawText("Name : " + player.getNickname(),1,0,0);
		drawText("Towers: "+ player.getNumOfTowers() + " Coins: " + player.getCoin(),1,12,0);
		if (player.getLastCardUsed() != null)
			drawText(" Played card: movements " + player.getLastCardUsed().getCardValue() + " - mother " + player.getLastCardUsed().getMovements(),1,13,0);

	}
}
