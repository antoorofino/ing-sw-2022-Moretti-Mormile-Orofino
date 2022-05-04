package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;

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
        for(Piece p : Piece.values()){
            this.studentsRoom.put(p,0);
        }
    }

    public ArrayList<Piece> getStudentsEntrance(){
        return new ArrayList<>(studentsEntrance);
    }


    public void removeFromEntrance(Piece student) throws SpecificStudentNotFoundException {
        if(this.studentsEntrance.contains(student))
            studentsEntrance.remove(student);
        else throw new SpecificStudentNotFoundException("Specific student isn't present");
    }

    public int getNumOfStudentsRoom(Piece student){
        return studentsRoom.get(student);
    }

    public void addToEntrance(ArrayList<Piece> students){
        this.studentsEntrance.addAll(students);
    }


    public void addStudentToRoom(Piece student) throws SpecificStudentNotFoundException {
        // can throw an exception
        // move automatically student from Entrance
        removeFromEntrance(student);
        studentsRoom.put(student,(studentsRoom.get(student)).intValue()+1);
    }

    public Map<Piece, Integer> getStudentsRoom(){return new HashMap<>(studentsRoom);}


}