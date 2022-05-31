package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Piece;

import java.util.ArrayList;

public class CLICloud extends CLIMatrix{

	public CLICloud(int id) {
		super(15,5, AnsiColor.ANSI_DEFAULT);
		drawBorder("╭╮─╰╯│");
		drawText(String.valueOf(id),1,1,12);
	}

	public void addStudent(ArrayList<Piece> students){
		int column = 2;
		for (Piece p:students) {
			elements[2][column].color = AnsiColor.getAnsiByPiece(p);
			elements[2][column].symbol = '●';
			column += 3;
		}
	}
}
