package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Map;

/**
 * Stores information about the bag of the game that contains
 * students
 */
public class Bag {
    private Map<Piece,Integer> students;

    /**
     * Constructor: Build the bag
     */
    public Bag(){
        for(Piece team : Piece.values() ){
                students.put(team,26);
        }
    }

    /**
     * Put student in the bag
     */
    public void addStudent(Piece student, int num){
        this.students.put(student,num);
    }

    /**
     * Check if bag is empty or not
     * @return true if bag is empty else false
     */
    public boolean isEmpty(){
        return students.isEmpty();
    }

    /**
     * Draw students randomly
     * @param
     * @return true if bag is empty else false
     */
    ArrayList<Piece> popStudents(int size){
        ArrayList<Piece> students = new ArrayList<Piece>();
        for(int i = 0 ; i < size ; i++){
            students.add(Piece.randomPiece());
        }
        return students;
    }

}
