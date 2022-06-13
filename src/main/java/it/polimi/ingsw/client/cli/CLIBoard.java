package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Map;

public class CLIBoard extends CLIMatrix {
	private final int entranceSize = 6;
	private final int teacherSize = 5;
	private String uuid;

	public CLIBoard(String uuid,int width) {
		super(width, 11,AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		this.uuid = uuid;
		drawBorder("╔╗═╚╝║");
		for (int i = 2; i < height - 1; i+=2)
			drawLine(i,entranceSize,width,"╠═╣");
		drawColumn(entranceSize,"╦╩╠═║╬");
		drawColumn(width-teacherSize,"╦╩╠═║╬");
	}

	public void addPieceEntrance(ArrayList<Piece> students){
		int row,column;
		row  = 1;
		column = 2;
		for (Piece p:students) {
			elements[row][column].color = AnsiColor.getAnsiByPiece(p);
			elements[row][column].symbol = '●';
			if(row!=9)
				row+=2;
			else{
				row=2;
				column=4;
			}
		}
	}

	public void drawDiningRoom(Map<Piece,Integer> students){
		for(Piece p: students.keySet()){
			for(int i=2;i<students.get(p)*3;i+=3){
				elements[getPiecePosition(p)][i+entranceSize].symbol = '●';
				elements[getPiecePosition(p)][i+entranceSize].color = AnsiColor.getAnsiByPiece(p);
			}
		}
	}

	public void drawTeachers(Map<Piece, Player> teachers){
		for(Piece p: teachers.keySet()){
			if(teachers.get(p).getId().equals(uuid)) { // possiedo il professore
				elements[getPiecePosition(p)][width - teacherSize + 2].symbol = '◍';
				elements[getPiecePosition(p)][width - teacherSize + 2].color = AnsiColor.getAnsiByPiece(p);
			}
		}
	}

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
