package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class IslandsHandler implements Serializable {
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
			island = new Island(i);
			if(i != motherNature && i != (motherNature + 6)%12){
				//FIXME: is it right to put i as index
				randPosition = rand.nextInt(studentsArray.size());
				island.addStudent(studentsArray.get(randPosition));
				studentsArray.remove(randPosition);
			}
			islandArrayList.add(island);
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
		throw new SpecificIslandNotFoundException("Cannot found island with this ID");
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

	public void mergeIsland(){
		int i = 0;
		do{
			if(islands.get(i).getIslandOwner() != null && islands.get(i+1).getIslandOwner() != null)
				if(islands.get(i).getIslandOwner().getNickname().equals(islands.get(i+1).getIslandOwner().getNickname())){
					moveValue(i + 1, i);
					i--;
					// shift id
					for(int j = i + 1; j < islands.size(); j++)
						islands.get(j).decreaseID();

				}
			i++;
		}while(i < islands.size() - 1);
		// last one
		if(islands.get(islands.size() - 1).getIslandOwner()!=null)
			if(islands.get(0).getIslandOwner()!=null)
				if(islands.get(0).getIslandOwner().getNickname().equals(islands.get(islands.size() - 1).getIslandOwner().getNickname()))
					moveValue(islands.size() - 1,0);
	}

	protected void moveValue(int from,int to){
		islands.get(to).increaseSize();
		for (Piece p: Piece.values()) {
			for(int n=0;n<islands.get(from).getNumStudents(p);n++)
				islands.get(to).addStudent(p);
		}
		islands.remove(from);
		if(motherNature == from)
			motherNature = to;
	}

}
