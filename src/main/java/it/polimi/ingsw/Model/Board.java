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

    //TODO implement method removeFromEntrance
    public void removeFromEntrance(Piece student){
        // generate exception
        studentsEntrance.remove(student);
    }

    public int getNumOfStudentsRoom(Piece student){
        return studentsRoom.get(student);
    }

    //TODO implement method addToEntrance
    public void addToEntrance(ArrayList<Piece> students){}

    //TODO implement method addStudentToDiningRoom
    public void addStudentToRoom(Piece student){
        // can throw an exception
        // move automatically student from Entrance
        removeFromEntrance(student);
        studentsRoom.put(student,studentsRoom.get(student)+1);
    }


}
