package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class BagTest {
    Bag bag;

    @BeforeEach
    public void setUp() {
        bag = new Bag();
    }

    @AfterEach
    public void tearDown() {
        bag = null;
    }

    @Test
    public void bagTest(){
        ArrayList<Piece> students = new ArrayList<>();
        assertFalse(bag.isEmpty());

        // remove all piece from bag
        while(!bag.isEmpty()){
            students.addAll(bag.popStudents(1));
        }

        assertTrue(bag.isEmpty());
        assertEquals(120,students.size());

        // check removed students
        for(Piece p : Piece.values()){
            assertEquals(24,Collections.frequency(students, p));
        }

        // add student to bag
        bag.addStudent(Piece.FROG,1);
        students = new ArrayList<>();

        while(!bag.isEmpty())
            students.addAll(bag.popStudents(1));

        assertEquals(1,students.size());
        assertEquals(Piece.FROG,students.get(0));

    }
}
