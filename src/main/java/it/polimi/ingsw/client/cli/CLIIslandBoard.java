package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Island;

import java.util.ArrayList;

public class CLIIslandBoard extends CLIMatrix{
	public CLIIslandBoard(int width, int height) {
		super(width, height,AnsiColor.ANSI_DEFAULT);
	}

	public void drawIslands(GameModel game){
		ArrayList<Island> islands = game.getIslandHandler().getIslands();
		int index = (12 - game.getIslandHandler().getLeftMerge())%12;
		final int[][] absolutePositions = { { 0, 14 }, { 0, 32 }, { 0, 50 }, { 0, 68 }, { 6, 84 }, { 15, 84 }, { 22, 68 }, { 22, 50 }, { 22, 32 }, { 22, 14 }, { 15, 0 }, { 6, 0 } };
		final int[][] relativePosition = { { -4, 12 }, { 0, 18 }, { 0, 18 }, { 0, 18 }, { 4, 12 }, { 7, 0 }, { 4, -12 }, { 0, -18 }, { 0, -18 }, { 0, -18 }, { -4, -12 }, { -7, -0 }};
		int x = 0;
		int y = 0;
		boolean mother;
		CLIIsland cliIsland;
		for(Island island:islands){
			mother = island.getID() == game.getIslandHandler().getMotherNature();
			for(int i = 0;i < island.getSize(); i++){
				cliIsland = new CLIIsland(16,mother, island.getID());
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
