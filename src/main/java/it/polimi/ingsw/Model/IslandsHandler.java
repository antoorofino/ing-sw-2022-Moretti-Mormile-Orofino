package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.SpecificIslandNotFoundException;

import java.util.ArrayList;

public class IslandsHandler {
	private ArrayList<Island> islands;
	private int motherNature;

	public IslandsHandler(){
		this.islands = new ArrayList<Island>();
		this.motherNature = 0;
	}
	public ArrayList<Island> getIslands() {
		return new ArrayList<>(islands);
	}

	public Island getIslandByID(int ID) throws SpecificIslandNotFoundException {
		for (Island island : islands) {
			if (island.getID() == ID)
				return island;
		}
		throw new SpecificIslandNotFoundException("Cannot found cloud with this ID");
	}

	public int getMotherNature() {
		return motherNature;
	}

	public void setIslands(ArrayList<Island> islands) {
		this.islands = islands;
	}

	public void moveMotherNature(int newPos) {
		if(islands.size()>0)
			this.motherNature = (this.motherNature + newPos)%(islands.size());
	}
}
