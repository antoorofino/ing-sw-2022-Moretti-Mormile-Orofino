package it.polimi.ingsw.Model;

import it.polimi.ingsw.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    ArrayList<Piece> studentsEntrance;
    Map<Piece,Integer> studentsRoom;

    /**
     * Constructor: Build the player's board
     */

    public Board(){
        this.studentsEntrance = new ArrayList<Piece>();
        this.studentsRoom = new HashMap<Piece,Integer>();
    }

    public ArrayList<Piece> getStudentsEntrance(){
        return this.studentsEntrance;
    }

    public int getNumOfStudentsRoom(Piece student){
        return 0;
    }

    //TODO implement method addToEntrance
    public void addToEntrance(ArrayList<Piece> students){}

    //TODO implement method addStudentToDiningRoom
    public void addStudentToDiningRoom(Piece student){}

    //TODO implement method getNumOfStudentDiningRoom
    public int getNumOfStudentDiningRoom(){ return 0;}




}
