package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.StudentException;

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


    public void removeFromEntrance(Piece student) throws StudentException {
        // generate exception
        if(this.studentsEntrance.isEmpty()) throw new StudentException("There aren't students in the entrance");
        studentsEntrance.remove(student);
    }

    public int getNumOfStudentsRoom(Piece student){
        return studentsRoom.get(student);
    }

    public void addToEntrance(ArrayList<Piece> students){
        this.studentsEntrance.addAll(students);
    }


    public void addStudentToRoom(Piece student) throws StudentException {
        // can throw an exception
        // move automatically student from Entrance
        if(this.studentsEntrance.isEmpty()) throw new StudentException("There aren't students in the entrance");
        removeFromEntrance(student);
        studentsRoom.put(student,studentsRoom.get(student)+1);
    }

    public Map<Piece, Integer> getStudentsRoom(){return this.studentsRoom;}


}
