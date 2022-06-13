package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.IslandsHandler;

import java.util.ArrayList;

public class CLIIslandBoard extends CLIMatrix{
	public CLIIslandBoard(IslandsHandler islandsHandler) {
		super(105, 26,AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		drawIslands(islandsHandler);
	}

    /*
		public CLIIslandBoard(){
		super(120, 40,AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		}

		public void drawIslands(int[] islandSize, int leftmerge){
		int index = (12 - leftmerge)%12; // countsLastMerge
		final int[][] absolutePositions = { { 0, 14 }, { 0, 34 }, { 0, 54 }, { 0, 74 }, { 5, 88 }, { 13, 88 }, { 18, 74 }, { 18, 54 }, { 18, 34 }, { 18, 14 }, { 13, 0 }, { 5, 0 } };
		final int[][] relativePosition = { { -3, 12 }, { 0, 16 }, { 0, 16 }, { 0, 16 }, { 3, 12 }, { 6, 0 }, { 3, -12 }, { 0, -16 }, { 0, -16 }, { 0, -16 }, { -3, -12 }, { -6, 0 }};
		int x = 0;
		int y = 0;
		int j = 0;
		int k=0;
		CLIIsland cliIsland;
		while(j<12){
			for(int i = 0;i < islandSize[k]; i++){
				cliIsland = new CLIIsland(false,k);
				if(i!=0){
					x += relativePosition[index][0];
					y += relativePosition[index][1];
				}else{
					x = absolutePositions[index][0];
					y = absolutePositions[index][1];
				}
				drawElement(x,y,cliIsland);
				index = (index + 1)%12;
				j++;
			}
			k++;
		}
	}
	public static void main(String[] args) {
		CLIIslandBoard cliIslandBoard = new CLIIslandBoard();
		int[] islands ={1,1,1,1,1,1,1,1,1,1,1,1};
		cliIslandBoard.drawIslands(islands,0);
		cliIslandBoard.display();

		cliIslandBoard.reset(AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		int[] islands1 = {1,2,2,1,2,1,1,2};
		cliIslandBoard.drawIslands(islands1,0);
		cliIslandBoard.display();

		cliIslandBoard.reset(AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		int[] islands2 = {2,2,2,2,2,2};
		cliIslandBoard.drawIslands(islands2,0);
		cliIslandBoard.display();

		cliIslandBoard.reset(AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		int[] islands3 = {5,7};
		cliIslandBoard.drawIslands(islands3,2);
		cliIslandBoard.display();

		cliIslandBoard.reset(AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		int[] islands4 = {2,2,2,2,2,2};
		cliIslandBoard.drawIslands(islands4,1);
		cliIslandBoard.display();

		cliIslandBoard.reset(AnsiColor.ANSI_DEFAULT,AnsiBackColor.ANSI_DEFAULT);
		int[] islands5 = {12};
		cliIslandBoard.drawIslands(islands5,-1);
		cliIslandBoard.display();

	}*/

	protected void drawIslands(IslandsHandler islandsHandler){
		ArrayList<Island> islands = islandsHandler.getIslands();
		int index = (12 - islandsHandler.getCountsLastMerge())%12; // countsLastMerge
		final int[][] absolutePositions = { { 0, 14 }, { 0, 34 }, { 0, 54 }, { 0, 74 }, { 5, 88 }, { 13, 88 }, { 18, 74 }, { 18, 54 }, { 18, 34 }, { 18, 14 }, { 13, 0 }, { 5, 0 } };
		final int[][] relativePosition = { { -3, 12 }, { 0, 16 }, { 0, 16 }, { 0, 16 }, { 3, 12 }, { 6, 0 }, { 3, -12 }, { 0, -16 }, { 0, -16 }, { 0, -16 }, { -3, -12 }, { -6, 0 }};
		int x = 0;
		int y = 0;
		boolean mother;
		CLIIsland cliIsland;
		for(Island island:islands){
			mother = island.getID() == islandsHandler.getMotherNature();
			for(int i = 0;i < island.getSize(); i++){
				cliIsland = new CLIIsland(mother, island.getID());
				if(island.getIslandOwner()!=null)
					cliIsland.drawOwner(island.getIslandOwner().getNickname());
				if(island.getSize()==1 || i==1)
					cliIsland.addStudents(island.getStudentsOnIsland());
				if(i!=0){
					x += relativePosition[index][0];
					y += relativePosition[index][1];
				}else{
					x = absolutePositions[index][0];
					y = absolutePositions[index][1];
				}
				drawElement(x,y,cliIsland);
				index = (index + 1)%12;
			}
		}
	}

}
