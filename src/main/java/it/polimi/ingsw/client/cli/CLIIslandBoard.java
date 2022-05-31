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
		int index = 0;
		int x = 0;
		int y = 14;
		boolean mother = false;
		CLIIsland cliIsland;
		for(Island island:islands){
			mother = island.getID() == game.getIslandHandler().getMotherNature();
			for(int i=0;i<island.getSize();i++){
				cliIsland = new CLIIsland(16,mother, island.getID());
				if(island.getIslandOwner()!=null)
					cliIsland.drawOwner(island.getIslandOwner().getNickname());
				if(island.getSize()==1 || i==1)
					cliIsland.addStudents(island.getStudentsOnIsland());
				switch(index){
					case 1:
					case 2:
					case 3:
						if(i!=0)
							y += 15;
						else
							y = 14 + index*18;
						break;
					case 4:
						if(i!=0){
							x = 3;
							y += 13;
						}
						else{
							x = 6;
							y = 82;
						}
						break;
					case 5:
						if(i!=0)
							x += 7;
						else{
							x = 15;
							y = 82;
						}
						break;
					case 6:
						if(i!=0){
							x += 3;
							y -= 13;
						}
						else{
							x = 22;
							y = 68;
						}
						break;
					case 7:
					case 8:
					case 9:
						if(i!=0)
							y -= 15;
						else {
							x = 22;
							y = 68 - (index-6)*18;
						}
						break;
					case 10:
						if(i!=0){
							y -= 13;
							x -= 3;
						}
						else{
							x = 15;
							y = 0;
						}
						break;
					case 11:
						if(i!=0)
							x -= 7;
						else{
							x = 6;
							y = 0;
						}
						break;
					default:
						break;
				}
				drawElement(x,y,cliIsland);
				index++;
			}
		}
	}
}
