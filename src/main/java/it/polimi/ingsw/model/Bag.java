package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores information about the bag of the game that contains students
 */
public class Bag implements Serializable {
    private final Map<Piece, Integer> students;

    /**
     * Constructor: Build the bag
     */
    public Bag() {
        students = new HashMap<Piece, Integer>();
        for (Piece piece : Piece.values()) {
            students.put(piece, 24);
        }
    }

    /**
     * Put student in the bag
     */
    public void addStudent(Piece student, int num) {
        int oldValue;
        oldValue = students.get(student);
        this.students.put(student, oldValue + num);
    }

    /**
     * Checks if bag is empty or not
     * @return true if bag is empty else false
     */
    public boolean isEmpty() {
        for (Piece piece : Piece.values()) {
            if (students.get(piece) > 0)
                return false;
        }
        return true;
    }

    /**
     * Remove students randomly
     * @param size number of students to remove
     * @return an arrayList that contains drawed student
     */
    public ArrayList<Piece> popStudents(int size) {
        ArrayList<Piece> studentsArray = new ArrayList<Piece>();
        int i = 0;
        int oldValue;
        Piece randomPiece;
        while ((i < size) && (!isEmpty())) {
            randomPiece = Piece.randomPiece();
            if ((students.get(randomPiece)) > 0) {
                studentsArray.add(randomPiece);
                oldValue = students.get(randomPiece);
                students.put(randomPiece, oldValue - 1);
                i++;
            }
        }
        return studentsArray;
    }
}
