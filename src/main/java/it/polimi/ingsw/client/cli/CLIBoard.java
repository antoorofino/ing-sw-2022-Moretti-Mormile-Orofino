package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.util.AnsiBackColor;
import it.polimi.ingsw.client.cli.util.AnsiColor;
import it.polimi.ingsw.model.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Creates a matrix of characters that contains a player's board
 */
public class CLIBoard extends CLIMatrix {
	private final int entranceSize = 6;
	private final int teacherSize = 5;
	private String uuid;

	/**
	 * Constructor: build player's board
	 * @param uuid the player id
	 * @param width the board width
	 */
	public CLIBoard(String uuid,int width) {
		super(width, 11, AnsiColor.ANSI_DEFAULT, AnsiBackColor.ANSI_DEFAULT);
		this.uuid = uuid;
		drawBorder("╔╗═╚╝║");
		for (int i = 2; i < height - 1; i+=2)
			drawLine(i,entranceSize,width,"╠═╣");
		drawColumn(entranceSize,"╦╩╠═║╬");
		drawColumn(width-teacherSize,"╦╩╠═║╬");
	}

	/**
	 * Draw the students at the entrance on the board
	 * @param students the student array
	 */
	public void addPieceEntrance(ArrayList<Piece> students){
		int row,column;
		row  = 2;
		column = 2;
		for (Piece p:students) {
			elements[row][column].color = AnsiColor.getAnsiByPiece(p);
			elements[row][column].symbol = '●';
			if(row!=8)
				row+=2;
			else{
				row=1;
				column=4;
			}
		}
	}

	/**
	 * Draw the students in the dining room on the board
	 * @param students the student map
	 */
	public void drawDiningRoom(Map<Piece,Integer> students){
		for(Piece p: students.keySet()){
			for(int i=2;i<students.get(p)*3;i+=3){
				elements[getPiecePosition(p)][i+entranceSize].symbol = '●';
				elements[getPiecePosition(p)][i+entranceSize].color = AnsiColor.getAnsiByPiece(p);
			}
		}
	}

	/**
	 * Draws the teachers owned by the player
	 * @param teachers the teacher map
	 */
	public void drawTeachers(List<Piece> teachers){
		for(Piece p: teachers){
			elements[getPiecePosition(p)][width - teacherSize + 2].symbol = '◉';
			elements[getPiecePosition(p)][width - teacherSize + 2].color = AnsiColor.getAnsiByPiece(p);
		}
	}

	/**
	 * Returns the row number for each student
	 * @param p the student
	 * @return the row number of specified student
	 */
	protected int getPiecePosition(Piece p){
		switch(p){
			case FROG:
				return 1;
			case DRAGON:
				return 3;
			case GNOME:
				return 5;
			case FAIRY:
				return 7;
			case UNICORN:
				return 9;
		}
		return 0;
	}
}
