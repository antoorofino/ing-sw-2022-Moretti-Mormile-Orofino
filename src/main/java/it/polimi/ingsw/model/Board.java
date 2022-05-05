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

    public int getNumOfStudentsRoom(Piece student){
        return studentsRoom.get(student);
    }

    public void addToEntrance(ArrayList<Piece> students){
        this.studentsEntrance.addAll(students);
    }

    public void removeFromEntrance(Piece student) throws SpecificStudentNotFoundException {
        if(this.studentsEntrance.contains(student))
            studentsEntrance.remove(student);
        else throw new SpecificStudentNotFoundException("Specific student isn't present");
    }

    // TODO: fix test methods calls after remove the automatically remove from entrance
    public void addStudentToRoom(Piece student) throws SpecificStudentNotFoundException {
        studentsRoom.put(student,(studentsRoom.get(student)).intValue()+1);
    }

    public void removeFromRoom(Piece student) throws SpecificStudentNotFoundException {
        if(this.studentsRoom.get(student).intValue() > 0 )
            studentsRoom.put(student,(studentsRoom.get(student)).intValue()-1);
        else throw new SpecificStudentNotFoundException("Specific student isn't present");
    }

    public Map<Piece, Integer> getStudentsRoom(){return new HashMap<>(studentsRoom);}


}
