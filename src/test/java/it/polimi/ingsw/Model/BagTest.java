package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.SpecificStudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    public void addStudentTest(){
        bag.addStudent(Piece.FROG,2);

    }
}
