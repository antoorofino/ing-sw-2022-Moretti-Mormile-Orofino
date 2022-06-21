package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BoardTest {
    Board board;

    @AfterEach
    public void tearDown() {
        board = null;
    }

    @Test
    public void getStudentsEntranceTest(){
        board = new Board();

        ArrayList<Piece> students = new ArrayList<>();
        students.add(Piece.GNOME);
        students.add(Piece.FROG);
        students.add(Piece.FAIRY);
        students.add(Piece.DRAGON);

        // add students to entrance
        board.addToEntrance(students);
        assertEquals(students,board.getStudentsEntrance());

        // remove student from entrance
        try {
            board.removeFromEntrance(Piece.GNOME);
        } catch (SpecificStudentNotFoundException ex) {
            fail();
        }
        students.remove(Piece.GNOME);
        assertEquals(students,board.getStudentsEntrance());

        // add student to dining room
        board.addStudentToRoom(Piece.FROG);
        assertEquals(students,board.getStudentsEntrance());

        // check students in the dining room
        assertEquals(1,board.getStudentsRoom().get(Piece.FROG));
    }
}
