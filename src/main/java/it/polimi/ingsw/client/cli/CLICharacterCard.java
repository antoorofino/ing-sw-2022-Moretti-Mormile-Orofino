package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Piece;

import java.util.ArrayList;

public class CLICharacterCard extends CLIMatrix{
	public CLICharacterCard(Character character) {
		super(10, 6, AnsiColor.ANSI_DEFAULT, AnsiBackColor.ANSI_DEFAULT);
		drawBorder("╭╮─╰╯│");
		drawStudentsCards(character.getStudents());
		if(character.getID() == 5)
			drawText("⛔ x" + character.getIslandFlag(),1,3,3);
		drawText(String.valueOf(character.getID()),1,1,1);
		drawText("©:" + character.getCost(),1,1,6);
		/*
		╭────────╮
		│ 1   ©:1│
		│  ●  ●  │
		│  ●  ●  │
		│  ●  ●  │
		╰────────╯
		 */
	}

	protected void drawStudentsCards(ArrayList<Piece> students){
		int column = 3;
		int row = 2;
		for (Piece p:students) {
			elements[row][column].color = AnsiColor.getAnsiByPiece(p);
			elements[row][column].symbol = '●';
			if(column!=6)
				column+= 3;
			else{
				column = 3;
				row+= 1;
			}
		}
	}
}
