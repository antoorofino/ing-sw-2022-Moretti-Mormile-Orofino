package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exception.SpecificCloudNotFoundException;
import it.polimi.ingsw.Exception.SpecificIslandNotFoundException;
import it.polimi.ingsw.Exception.SpecificStudentNotFoundException;
import it.polimi.ingsw.Exception.StudentNotPresentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CharacterTest {
    Character character;

    @BeforeEach
    public void setUp() {

    }


    @AfterEach
    public void tearDown() {
        character = null;
    }

    @Test
    public void getNameTest(){
        Rules r = new Rules();
        character = new Character("name1","description1",1,0,r);
        assertEquals("name1",character.getName());
    }
    @Test
    public void getDescriptionTest(){
        Rules r = new Rules();
        character = new Character("name1","description1",1,0,r);
        assertEquals("description1",character.getDescription());
    }
    @Test
    public void getIDTest(){
        Rules r = new Rules();
        character = new Character("name1","description1",1,0,r);
        assertEquals(0,character.getID());
    }
    @Test
    public void getRulesTest(){
        Rules r = new Rules();
        character = new Character("name1","description1",1,0,r);
        assertEquals(r,character.getRules());
    }
    @Test
    public void getIslandFlagTest(){
        Rules r = new Rules();
        character = new Character("name1","description1",1,0,r);
        character.setIslandFlag(1);
        assertEquals(1,character.getIslandFlag());
    }

    @Test
    public void StudentsTest(){
        Rules r = new Rules();
        character = new Character("name1","description1",1,0,r);
        ArrayList<Piece> students = new ArrayList<Piece>();
        assertEquals(students,character.getStudents());
        students.add(Piece.FROG);
        students.add(Piece.FAIRY);
        character.addStudents(students);
        assertEquals(students,character.getStudents());
        try {
            character.delStudent(Piece.FROG);
        } catch (StudentNotPresentException e) {
            e.printStackTrace();
        } catch (SpecificStudentNotFoundException e) {
        }
        students.remove(Piece.FROG);
        assertEquals(students,character.getStudents());

        try {
            character.delStudent(Piece.UNICORN);
        } catch (StudentNotPresentException e) {
            e.printStackTrace();
        } catch (SpecificStudentNotFoundException e) {
        }
    }

    @Test
    public void costTest(){
        Rules r = new Rules();
        character = new Character("name1","description1",1,0,r);
        assertEquals(1,character.getCost());
        character.increaseCost();
        assertEquals(2,character.getCost());
    }



}
