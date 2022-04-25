package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.SpecificStudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
        Map<Piece,Integer> studentsTest = new HashMap<Piece,Integer>();
        for(Piece p : Piece.values())
            studentsTest.put(p,0);

        ArrayList<Piece> studentsRandom = new ArrayList<Piece>();
        int oldValue;
        int cnt=0;
        assertEquals(false,bag.isEmpty());
        while(!bag.isEmpty()){
            studentsRandom=bag.popStudents(1);
            oldValue=studentsTest.get(studentsRandom.get(0));
            studentsTest.put(studentsRandom.get(0),oldValue+1);

        }
        System.out.println(studentsTest);
        assertEquals(true,bag.isEmpty());
        //assertEquals(120,studentsRandom.size());
        for(Piece p : Piece.values())
            assertEquals(24,studentsTest.get(p).intValue());

        bag.addStudent(Piece.FROG,1);
        studentsRandom=new ArrayList<Piece>();
        while(!bag.isEmpty())
            studentsRandom.addAll(bag.popStudents(1));

        assertEquals(1,studentsRandom.size());
        assertEquals(Piece.FROG,studentsRandom.get(0));

    }
}
