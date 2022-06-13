package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Piece;

import java.util.ArrayList;

public class CLICloud extends CLIMatrix{

	public CLICloud(Cloud cloud) {
		super(15, 4, AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		   drawText("╭─────╮", 1, 0, 3);
		 drawText("╭─╯     ╰───╮", 1, 1, 1);
		drawText("╭╯           ╰╮", 1, 2, 0);
		drawText("╰─────────────╯", 1, 3, 0);
		drawText(String.valueOf(cloud.getCloudID()), 1, 0, 11);
		drawStudent(cloud.getStudents());
	}

	protected void drawStudent(ArrayList<Piece> students){
		int column = 3;
		for (Piece p:students) {
			elements[2][column].color = AnsiColor.getAnsiByPiece(p);
			elements[2][column].symbol = '●';
			if(students.size() != 3 )
				column+= 2;
			else
				column+= 3;
		}
	}
}
