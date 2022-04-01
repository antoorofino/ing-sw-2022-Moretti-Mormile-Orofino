package it.polimi.ingsw.Model;

import java.awt.*;
import java.util.Map;

public class Island {
    private Map<Piece, Integer> studentsOnIsland;
    private int ID;
    private int size;
    private Player islandOwner;
    private boolean flagNoInfluence;

    public boolean towerIsAlreadyBuild(){
        return islandOwner.equals(null);
    }

    public int getNumStudents(Piece s){ return 0;}

    //TODO implement method addStudent
    public void addStudent(Piece s){}

    public Player getIslandOwner(){return islandOwner;}

    public int getSize(){return size;}

    //TODO implement method calculateInfluence
    public int calculateInfluence(TeachersHandler teacher, boolean towerCount, Color invalidColor){return 0;}


}
