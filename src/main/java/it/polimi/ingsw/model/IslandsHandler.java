package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Helper class to manage island
 */
public class IslandsHandler implements Serializable {
	private ArrayList<Island> islands;
	private int motherNature;
	private int countsLastMerge;

	/**
	 * Constructor: builds IslandHandler
	 */
	public IslandsHandler(){
		this.islands = new ArrayList<>();
		this.motherNature = 0;
		this.countsLastMerge = 0;
	}

	/**
	 * Initializes the islands when create game
	 */
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
				randPosition = rand.nextInt(studentsArray.size());
				island.addStudent(studentsArray.get(randPosition));
				studentsArray.remove(randPosition);
			}
			islandArrayList.add(island);
		}
		this.islands = islandArrayList;
	}

	/**
	 * Initializes the islands for testing
	 */
	public void setupIslandsTest(){
		ArrayList<Piece> studentsArray = new ArrayList<>();
		ArrayList<Island> islandArrayList = new ArrayList<>();
		Island island;
		motherNature = 0;
		for(int i = 0; i < 12; i++){
			island = new Island(i);
			if(i != getMotherNature() && i != (getMotherNature() + 6)%12){
				island.addStudent(Piece.DRAGON);
			}
			islandArrayList.add(island);
		}
		this.islands = islandArrayList;
	}

	/**
	 * Gets list of islands
	 * @return list of islands
	 */
	public ArrayList<Island> getIslands() {
		return new ArrayList<>(islands);
	}

	/**
	 * Gets specific island by ID
	 * @param ID id of island
	 * @return island that has the specific ID
	 * @throws SpecificIslandNotFoundException invalid island id
	 */
	public Island getIslandByID(int ID) throws SpecificIslandNotFoundException {
		for (Island island : islands) {
			if (island.getID() == ID)
				return island;
		}
		throw new SpecificIslandNotFoundException("Cannot found island with this id");
	}

	/**
	 * Gets the island with mother nature
	 * @return the island
	 */
	public Island getCurrentIsland(){
		return islands.get(motherNature);
	}

	/**
	 * Gets the position of mother nature
	 * @return position of mother nature
	 */
	public int getMotherNature() {
		return motherNature;
	}

	/**
	 * Moves motherNature
	 * @param steps specify how much steps mother nature has to do
	 */
	public void moveMotherNature(int steps) {
		if(islands.size()>0)
			this.motherNature = (this.motherNature + steps)%(islands.size());
	}

	/**
	 * Does the merge of island
	 */
	public void mergeIslands(){
		int i = 0;
		do{
			if(islands.get(i).getIslandOwner() != null && islands.get(i+1).getIslandOwner() != null)
				if(islands.get(i).getIslandOwner().getNickname().equals(islands.get(i+1).getIslandOwner().getNickname())){
					if(motherNature > i)
						moveMotherNature(-1);
					moveValue(i + 1, i);
					// shift id
					for(int j = i + 1; j < islands.size(); j++)
						islands.get(j).decreaseID();
					i--;
				}
			i++;
		}while(i < islands.size() - 1);
		// last one
		if(islands.get(islands.size() - 1).getIslandOwner()!=null)
			if(islands.get(0).getIslandOwner()!=null)
				if(islands.get(0).getIslandOwner().getNickname().equals(islands.get(islands.size() - 1).getIslandOwner().getNickname())){
					// leftmerge mi effettua uno shift grafico a sx
					countsLastMerge += islands.get(islands.size() - 1).getSize();
					if(motherNature == (islands.size() - 1))
						moveMotherNature(1);
					moveValue(islands.size() - 1,0);
				}
	}

	/**
	 * Moves points on Island to another when we do merge
	 * @param from departure island
	 * @param to arrival island
	 */
	protected void moveValue(int from,int to){
		islands.get(to).increaseSize(islands.get(from).getSize());
		for (Piece p: Piece.values()) {
			for(int n = 0; n < islands.get(from).getNumStudents(p); n++)
				islands.get(to).addStudent(p);
		}
		for (int i = 0; i < islands.get(from).getFlagNoInfluence(); i++) {
			islands.get(to).addFlagNoInfluence();
		}
		islands.remove(from);
	}

	/**
	 * Helper method used for print merge
	 * @return number of merge between last and fist of the list
	 */
	public int getCountsLastMerge(){ return this.countsLastMerge;}

}
