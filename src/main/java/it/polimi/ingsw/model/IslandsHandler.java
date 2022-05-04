package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;

import java.util.ArrayList;
import java.util.Random;

public class IslandsHandler {
	private ArrayList<Island> islands;
	private int motherNature;

	public IslandsHandler(){
		this.islands = new ArrayList<>();
		this.motherNature = 0;
	}

	public void setupIslands(){
		ArrayList<Piece> studentsArray = new ArrayList<>();
		ArrayList<Island> islandArrayList = new ArrayList<>();
		Island island;
		Random rand = new Random();
		int randPosition;
		for(Piece piece : Piece.values() ){
			studentsArray.add(piece);
			studentsArray.add(piece);
		}
		this.motherNature = rand.nextInt(12);
		for(int i = 0; i < 12; i++){
			if(i != motherNature && i != (motherNature + 6)%12){
				//FIXME: is it right to put i as index
				island = new Island(i);
				randPosition = rand.nextInt(studentsArray.size());
				island.addStudent(studentsArray.get(randPosition));
				studentsArray.remove(randPosition);
				islandArrayList.add(island);
			}
		}
		this.setIslands(islandArrayList);
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
