package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores information about player's board
 */
public class Board implements Serializable {
    ArrayList<Piece> studentsEntrance;
    Map<Piece,Integer> studentsRoom;

    /**
     * Constructor: Builds the player's board
     */
    public Board(){
        this.studentsEntrance = new ArrayList<Piece>();
        this.studentsRoom = new HashMap<Piece,Integer>();
        for(Piece p : Piece.values()){
            this.studentsRoom.put(p,0);
        }
    }

    /**
     * Gets all the student at the board's entrance
     * @return an array list that contains the students in the entrance
     */
    public ArrayList<Piece> getStudentsEntrance(){
        return new ArrayList<>(studentsEntrance);
    }

    /**
     * Adds students in the entrance
     */
    public void addToEntrance(List<Piece> students){
        this.studentsEntrance.addAll(students);
    }

    /**
     * Removes a students from the entrance
     * @param student student to remove
     * @throws SpecificStudentNotFoundException student not found at the entrance
     */
    public void removeFromEntrance(Piece student) throws SpecificStudentNotFoundException {
        if(this.studentsEntrance.contains(student))
            studentsEntrance.remove(student);
        else throw new SpecificStudentNotFoundException("Specific student isn't present");
    }

    /**
     * Gets how many students are in the room
     * @return number of students in the room
     */
    public int getNumOfStudentsRoom(Piece student){
        return studentsRoom.get(student);
    }

    /**
     * Adds students in the room and automatically remove it from entrance
     * @param student student to remove
     */
    public void addStudentToRoom(Piece student){
        studentsRoom.put(student, studentsRoom.get(student) + 1);
    }

    /**
     * Removes student from dining room
     * @param student student to remove
     * @throws SpecificStudentNotFoundException student not found in the dining room
     */
    public void removeFromRoom(Piece student) throws SpecificStudentNotFoundException {
        if(this.studentsRoom.get(student) > 0 )
            studentsRoom.put(student, studentsRoom.get(student) - 1);
        else throw new SpecificStudentNotFoundException("Specific student isn't present");
    }

    /**
     * Gets how many students are in the room for each type
     * @return map that contains how many students for each type are in the room
     */
    public Map<Piece, Integer> getStudentsRoom(){
        return new HashMap<>(studentsRoom);
    }
}
