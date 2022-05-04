package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

    Board board;

    @BeforeEach
    public void setUp() {
    }

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

        board.addToEntrance(students);
        assertEquals(students,board.getStudentsEntrance());

        try {
            board.removeFromEntrance(Piece.GNOME);
        } catch (SpecificStudentNotFoundException ignored) {

        }
        students.remove(Piece.GNOME);
        assertEquals(students,board.getStudentsEntrance());

        try {
            board.addStudentToRoom(Piece.FROG);
        } catch (SpecificStudentNotFoundException ignored) {

        }
        students.remove(Piece.FROG);
        assertEquals(students,board.getStudentsEntrance());

        Map<Piece,Integer> studentsRoom = new HashMap<>();
        for(Piece p : Piece.values()){
            if(p.equals(Piece.FROG))
                studentsRoom.put(Piece.FROG,1);
            else
                studentsRoom.put(p,0);
        }
        assertEquals(studentsRoom,board.getStudentsRoom());
        try {
            board.addStudentToRoom(Piece.UNICORN);
        } catch (SpecificStudentNotFoundException ignored) {

        }
    }
}
