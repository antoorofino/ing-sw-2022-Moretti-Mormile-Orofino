package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Piece;

import java.util.Map;

public class CLIIsland extends CLIMatrix {
	public CLIIsland(boolean mother,int id) {
		super(17, 7,mother? AnsiColor.ANSI_YELLOW : AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		drawText("    ┌───────┐",1,0,0);
		drawText("  ┌─┘       └─┐",1,1,0);
		drawText("┌─┘           └─┐",1,2,0);
		drawText("│               │",1,3,0);
		drawText("└─┐           ┌─┘",1,4,0);
		drawText("  └─┐       ┌─┘",1,5,0);
		drawText("    └───────┘",1,6,0);
		drawId(id);

		/*
	     └┘─│ ┤├
	      ┏ ┓ ┗ ━ ┛ ┃
             ┌───────┐
		   ┌─┘   1   └─┐
		 ┌─┘ ● x1 ● x2 └─┐
		 │  ● x2   ● x2  │
		 └─┐   ● x3    ┌─┘
		   └─┐ matte ┌─┘
		     └───────┘
		 */

	}

	public void addStudents(Map<Piece, Integer> students) {
		final int[][] studentsPositions = { { 2, 4 }, { 2, 9 }, { 3, 3 }, { 3, 10 }, { 4, 6 }};
		int i = 0;
		int row;
		int column;
		for (Piece p : Piece.values()) {
			row = studentsPositions[i][0];
			column = studentsPositions[i][1];
			if((students.get(p)!=null)&&(students.get(p)!=0)) {
				elements[row][column].symbol = '●';
				elements[row][column].color = AnsiColor.getAnsiByPiece(p);
				elements[row][column+2].symbol = 'x';
				elements[row][column+3].symbol = Integer.toString(students.get(p)).charAt(0);
			}
			i++;
		}
	}

	protected void drawId(int id){
		if(id > 9){
			elements[1][10].symbol = String.valueOf(id /10).charAt(0);
		}
		elements[1][11].symbol = String.valueOf(id %10).charAt(0);
	}

	public void drawOwner(String owner){
		this.drawText(owner.substring(0,4),1,height/2,2*width/3);
	}
}
