package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherHandlerTest {
    TeachersHandler teachersHandler;

    @BeforeEach
    public void setUp() {
        teachersHandler = new TeachersHandler();
    }


    @AfterEach
    public void tearDown() {
        teachersHandler = null;
    }

    @Test

    public void calculateInfluenceTest(){
        Player p1 = new Player("id_p1");
        Player p2 = new Player("id_p2");
        Player p3 = new Player("id_p3");
        p1.setNickname("nick1");
        p2.setNickname("nick2");
        p3.setNickname("nick3");
        // Add players
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        /*
        ----------------------------------------------------------------
        P1 has 2 frog e 1fairy in the room
        ----------------------------------------------------------------
        */
        p1.getPlayerBoard().addStudentToRoom(Piece.FROG);
        p1.getPlayerBoard().addStudentToRoom(Piece.FROG);
        p1.getPlayerBoard().addStudentToRoom(Piece.FAIRY);

        //geq = false
        teachersHandler.calculateTeacher(players,false);
        //Teacher is assigned
        assertTrue(teachersHandler.teacherIsAssigned(Piece.FROG));
        assertTrue(teachersHandler.teacherIsAssigned(Piece.FAIRY));
        //Teacher owner
        assertEquals(p1,teachersHandler.getTeacherOwner(Piece.FROG));
        assertEquals(p1,teachersHandler.getTeacherOwner(Piece.FAIRY));

        /*
        ----------------------------------------------------------------
        P2 has 1 gnome and 1 fairy in the room
        ----------------------------------------------------------------
        */
        p2.getPlayerBoard().addStudentToRoom(Piece.GNOME);
        p2.getPlayerBoard().addStudentToRoom(Piece.FAIRY);
        p2.getPlayerBoard().addStudentToRoom(Piece.FAIRY);

        //geq = false
        teachersHandler.calculateTeacher(players,false);
        //Teacher is assigned
        assertTrue(teachersHandler.teacherIsAssigned(Piece.FROG));
        assertTrue(teachersHandler.teacherIsAssigned(Piece.FAIRY));
        assertTrue(teachersHandler.teacherIsAssigned(Piece.GNOME));
        //Teacher owner
        assertEquals(p1,teachersHandler.getTeacherOwner(Piece.FROG));
        assertEquals(p2,teachersHandler.getTeacherOwner(Piece.GNOME));
        assertEquals(p2,teachersHandler.getTeacherOwner(Piece.FAIRY));

        /*
        ----------------------------------------------------------------
        P3 has 2 dragon and 1 gnome in the room
        ----------------------------------------------------------------
         */
        p3.getPlayerBoard().addStudentToRoom(Piece.DRAGON);
        p3.getPlayerBoard().addStudentToRoom(Piece.DRAGON);
        p3.getPlayerBoard().addStudentToRoom(Piece.GNOME);

        //geq = true
        teachersHandler.calculateTeacher(players,true);
        //Teacher is assigned
        assertTrue(teachersHandler.teacherIsAssigned(Piece.FROG));
        assertTrue(teachersHandler.teacherIsAssigned(Piece.GNOME));
        assertTrue(teachersHandler.teacherIsAssigned(Piece.FAIRY));
        assertTrue(teachersHandler.teacherIsAssigned(Piece.DRAGON));
        assertFalse(teachersHandler.teacherIsAssigned(Piece.UNICORN));
        //Teacher owner
        assertEquals(p1,teachersHandler.getTeacherOwner(Piece.FROG));
        assertEquals(p2,teachersHandler.getTeacherOwner(Piece.FAIRY));
        assertEquals(p3,teachersHandler.getTeacherOwner(Piece.GNOME));
        assertEquals(p3,teachersHandler.getTeacherOwner(Piece.DRAGON));

        // teachers controlled test
        assertEquals(1,teachersHandler.teachersControlled(p1));
        assertEquals(1,teachersHandler.teachersControlled(p2));
        assertEquals(2,teachersHandler.teachersControlled(p3));

        // getTeachersByPlayer test
        assertTrue(Arrays.asList(Piece.GNOME,Piece.DRAGON).containsAll(teachersHandler.getTeachersByPlayerId(p3.getId())));
    }

    @Test
    public void teacherIsAssignedTest(){
        for(Piece p : Piece.values()){
            assertEquals(false,teachersHandler.teacherIsAssigned(p));
        }
    }

}