package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        p1.setNickname("1");
        p2.setNickname("2");
        p3.setNickname("3");

        /*
        ----------------------------------------------------------------
        P1 has 1gnome 1frog e 1fairy 1dragon, 1frog e 1fairy in the room
        ----------------------------------------------------------------
         */
        ArrayList<Piece> students = new ArrayList<>();
        students.add(Piece.GNOME);
        students.add(Piece.FROG);
        students.add(Piece.FAIRY);
        students.add(Piece.DRAGON);
        p1.getPlayerBoard().addToEntrance(students);
        try {
            p1.getPlayerBoard().addStudentToRoom(Piece.FROG);
            p1.getPlayerBoard().addStudentToRoom(Piece.FAIRY);
        } catch (SpecificStudentNotFoundException e) {
            //fail();
        }
        /*
        ----------------------------------------------------------------
        P2 has 1gnome 2frog e 1fairy, 1gnome and 1fairy in the room
        ----------------------------------------------------------------
         */
        students.add(Piece.GNOME);
        students.add(Piece.FROG);
        students.add(Piece.FAIRY);
        students.add(Piece.FROG);
        p2.getPlayerBoard().addToEntrance(students);
        try {
            p2.getPlayerBoard().addStudentToRoom(Piece.GNOME);
            p2.getPlayerBoard().addStudentToRoom(Piece.FAIRY);
        } catch (SpecificStudentNotFoundException e) {
            //fail();
        }

        /*
        ----------------------------------------------------------------
        P3 has 1gnome 2frogs e 1fairy 2 dragons, 2frog in the room
        ----------------------------------------------------------------
         */
        students.add(Piece.FROG);
        students.add(Piece.GNOME);
        students.add(Piece.FROG);
        students.add(Piece.FAIRY);
        students.add(Piece.DRAGON);
        students.add(Piece.DRAGON);
        p3.getPlayerBoard().addToEntrance(students);
        try {
            p3.getPlayerBoard().addStudentToRoom(Piece.FROG);
            p3.getPlayerBoard().addStudentToRoom(Piece.FROG);
        } catch (SpecificStudentNotFoundException e) {
            //fail();
        }

        //Analyze player 1 and 2
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        //geq=false
        teachersHandler.calculateTeacher(players,false);
        //Teacher is assigned
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.FROG));
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.GNOME));
        assertEquals(false,teachersHandler.teacherIsAssigned(Piece.FAIRY));
        //Teacher owner
        assertEquals(p1,teachersHandler.getTeacherOwner(Piece.FROG));
        assertEquals(p2,teachersHandler.getTeacherOwner(Piece.GNOME));
        assertEquals(null,teachersHandler.getTeacherOwner(Piece.FAIRY));

        /*
        -------------------------------------------------------------------------------
         */
        //geq = true
        teachersHandler.calculateTeacher(players,true);
        //Teacher is assigned
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.FROG));
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.GNOME));
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.FAIRY));
        //Teacher owner
        assertEquals(p1,teachersHandler.getTeacherOwner(Piece.FROG));
        assertEquals(p2,teachersHandler.getTeacherOwner(Piece.GNOME));
        assertEquals(p1,teachersHandler.getTeacherOwner(Piece.FAIRY));

        /*
        -------------------------------------------------------------------------------
         */

        /*
        ----------------------------------------------------------------
        P2 has 1gnome 2frog e 1fairy, 1gnome 2frogs and 1fairy in the room
        ----------------------------------------------------------------
         */
        try {
            p2.getPlayerBoard().addStudentToRoom(Piece.FROG);
            p2.getPlayerBoard().addStudentToRoom(Piece.FROG);
        } catch (SpecificStudentNotFoundException ignored) {

        }
        /*
        ----------------------------------------------------------------
        P1 has 1gnome 1frog e 1fairy 1dragon, 1frog e 1fairy in the room
        ----------------------------------------------------------------
         */
        /*
        try {
            p1.getPlayerBoard().addStudentToRoom(Piece.DRAGON);
        } catch (SpecificStudentNotFoundException e) {

        }
         */


        players=new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        /*
        P1 has 1gnome 1frog e 1fairy 1dragon,
        1frog e 1fairy in the room

        P2 has 1gnome 2frog e 1fairy,
        1gnome 2frogs and 1fairy in the room

        P3 has 1gnome 2frogs e 1fairy 2 dragons,
        2frog in the room

        frog null, gnome p2, fairy null
         */

        teachersHandler.calculateTeacher(players,false);

        assertEquals(false,teachersHandler.teacherIsAssigned(Piece.FROG));
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.GNOME));
        assertEquals(false,teachersHandler.teacherIsAssigned(Piece.FAIRY));

        assertEquals(null,teachersHandler.getTeacherOwner(Piece.FROG));
        assertEquals(p2,teachersHandler.getTeacherOwner(Piece.GNOME));
        assertEquals(null,teachersHandler.getTeacherOwner(Piece.FAIRY));

        teachersHandler.calculateTeacher(players,true);
         /*
        P1 has 1gnome 1frog e 1fairy 1dragon,
        1frog e 1fairy in the room

        P2 has 1gnome 2frog e 1fairy,
        1gnome 2frogs and 1fairy in the room

        P3 has 1gnome 2frogs e 1fairy 2 dragons,
        2frogs in the room

        frog p3, gnome p2, fairy p2
         */

        teachersHandler.calculateTeacher(players,true);
        //Teacher is assigned
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.FROG));
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.GNOME));
        assertEquals(true,teachersHandler.teacherIsAssigned(Piece.FAIRY));

        //Teacher owner
        assertEquals(p3,teachersHandler.getTeacherOwner(Piece.FROG));
        assertEquals(p2,teachersHandler.getTeacherOwner(Piece.GNOME));
        assertEquals(p2,teachersHandler.getTeacherOwner(Piece.FAIRY));


    }

    @Test
    public void teacherIsAssignedTest(){
        for(Piece p : Piece.values()){
            assertEquals(false,teachersHandler.teacherIsAssigned(p));
        }
    }

}