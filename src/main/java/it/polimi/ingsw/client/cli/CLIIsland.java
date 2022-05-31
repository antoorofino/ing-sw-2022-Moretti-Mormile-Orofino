package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Piece;

import java.util.Map;

public class CLIIsland extends CLIMatrix {
	public CLIIsland(int width,boolean mother,int id) {
		super(width, width / 2,mother? AnsiColor.ANSI_YELLOW : AnsiColor.ANSI_DEFAULT);
		for (int i = width / 4; i < width - width / 4; i++) {
			elements[0][i].symbol = '_';
			elements[height - 1][i].symbol = '_';
		}

		for (int i = 1; i < width / 4; i++) {
			elements[i][width / 4 - i].symbol = '/';
			elements[height / 2 + i][i].symbol = '\\';
			elements[i][3 * width / 4 + (i - 1)].symbol = '\\';
			elements[width / 4 + i][width - (i + 1)].symbol = '/';
		}

		elements[height / 2][width - 1].symbol = '⎸';
		elements[height / 2][1].symbol = '⎸';
		//elements[height / 2][width - 1].symbol = '|';
		//elements[height / 2][1].symbol = '|';

		drawId(id);
	}

	public void addStudents(Map<Piece, Integer> students) {
		int row = height/4 ;
		int column = width/4;
		for (Piece p : Piece.values()) {
			if((students.get(p)!=null)&&(students.get(p)!=0)) {
				elements[row][column].symbol = '●';
				elements[row][column].color = AnsiColor.getAnsiByPiece(p);
				elements[row][column+2].symbol = 'x';
				elements[row][column+3].symbol = Integer.toString(students.get(p)).charAt(0);
			}
			row++;
		}
	}

	protected void drawId(int id){
		if(id > 9){
			elements[1][width - (width/4+2)].symbol = String.valueOf(id /10).charAt(0);
		}
		elements[1][width - (width/4+1)].symbol = String.valueOf(id %10).charAt(0);
	}

	public void drawOwner(String owner){
		this.drawText(owner.substring(0,4),1,height/2,2*width/3);
	}
}
