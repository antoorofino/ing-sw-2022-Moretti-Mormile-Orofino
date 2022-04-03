package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.CloudException;

import java.util.ArrayList;

public class IslandsHandler {
	private ArrayList<Island> islands;
	int motherNature;

	public ArrayList<Island> getIslands() {
		return islands;
	}

	public Island getIslandByID(int ID) throws CloudException {
		for (Island island : islands) {
			if (island.getID() == ID)
				return island;
		}
		throw new CloudException("Cannot found cloud with this ID");
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
