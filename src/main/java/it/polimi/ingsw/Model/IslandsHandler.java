package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class IslandsHandler {
	private ArrayList<Island> islands;
	int motherNature;

	public ArrayList<Island> getIslands() {
		return islands;
	}

	public Island getIslandByID(int ID) {
		for (Island island : islands) {
			if (island.getID() == ID)
				return island;
		}
		// TODO throw exception
		return null;
	}

	public int getMotherNature() {
		return motherNature;
	}

	public void setIslands(ArrayList<Island> islands) {
		this.islands = islands;
	}

	public void setMotherNature(int newPos) {
		this.motherNature = newPos;
	}
}
