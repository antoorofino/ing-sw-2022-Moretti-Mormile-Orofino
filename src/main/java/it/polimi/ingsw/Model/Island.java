package it.polimi.ingsw.Model;

import java.awt.*;
import java.util.Map;

public class Island {
    private Map<Piece, Integer> studentsOnIsland;
    private int ID;
    private int size;
    private Player islandOwner;
    private boolean flagNoInfluence;

    public Island(int islandID){
        //this.studentsOnIsland = new Map<Piece, Integer>();
        this.ID=islandID;
        this.islandOwner = new Player();
        this.size = 1;
        this.flagNoInfluence = false;
    }

    public boolean towerIsAlreadyBuild(){
        return islandOwner.equals(null);
    }

    public int getNumStudents(Piece s){
        return studentsOnIsland.get(s);
    }

    //TODO check the implementation
    public void addStudent(Piece s){
        studentsOnIsland.put(s,studentsOnIsland.get(s)+1);
    }

    public Player getIslandOwner(){
        return islandOwner;
    }

    public int getSize(){
        return size;
    }

    //TODO implement method calculateInfluence
    public Player calculateInfluence(TeachersHandler teacher, boolean towerCount, Piece invalidColor){
    // Good luck
        //teacher handler per vedere chi possiede il prof
        //tower count mi dice se devo contare le torri
        //invalid color al limite un colore che non mi serve
        return null;
    }

    public int getID(){
        return this.ID;
    }

}
