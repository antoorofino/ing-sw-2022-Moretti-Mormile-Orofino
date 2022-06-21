package it.polimi.ingsw.model;

import it.polimi.ingsw.server.rules.Rules;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import it.polimi.ingsw.util.exception.StudentNotPresentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class CharacterTest {
    Character character;

    @AfterEach
    public void tearDown() {
        character = null;
    }

    @Test
    public void getNameTest(){
        Rules r = new Rules(new GameModel(new GameListInfo("name", GameMode.BASIC,3)));
        character = new Character("name1","description1",1,0,r);
        assertEquals("name1",character.getName());
    }
    @Test
    public void getDescriptionTest(){
        Rules r = new Rules(new GameModel(new GameListInfo("name", GameMode.BASIC,3)));
        character = new Character("name1","description1",1,0,r);
        assertEquals("description1",character.getDescription());
    }
    @Test
    public void getIDTest(){
        Rules r = new Rules(new GameModel(new GameListInfo("name", GameMode.BASIC,3)));
        character = new Character("name1","description1",1,0,r);
        assertEquals(0,character.getID());
    }
    @Test
    public void getRulesTest(){
        Rules r = new Rules(new GameModel(new GameListInfo("name", GameMode.BASIC,3)));
        character = new Character("name1","description1",1,0,r);
        assertEquals(r,character.getRules());
    }
    @Test
    public void getIslandFlagTest(){
        Rules r = new Rules(new GameModel(new GameListInfo("name", GameMode.BASIC,3)));
        character = new Character("name1","description1",1,0,r);
        character.setIslandFlag(1);
        assertEquals(1,character.getIslandFlag());
    }

    @Test
    public void studentsTest(){
        // create e initialise character card
        Rules r = new Rules(new GameModel(new GameListInfo("name", GameMode.BASIC,3)));
        character = new Character("name1","description1",1,0,r);
        ArrayList<Piece> students = new ArrayList<>();
        assertEquals(students,character.getStudents());
        students.add(Piece.FROG);
        students.add(Piece.FAIRY);
        character.addStudents(students);

        // check students on card
        assertEquals(students,character.getStudents());
        // remove student from card
        try {
            character.delStudent(Piece.FROG);
        } catch (StudentNotPresentException | SpecificStudentNotFoundException e) {
            fail();
        }
        students.remove(Piece.FROG);
        assertEquals(students,character.getStudents());
    }

    @Test
    public void costTest(){
        Rules r = new Rules(new GameModel(new GameListInfo("name", GameMode.BASIC,3)));
        character = new Character("name1","description1",1,0,r);
        assertEquals(1,character.getCost());
        character.increaseCost();
        assertEquals(2,character.getCost());
        // increase only first time
        character.increaseCost();
        assertEquals(2,character.getCost());
    }
}
