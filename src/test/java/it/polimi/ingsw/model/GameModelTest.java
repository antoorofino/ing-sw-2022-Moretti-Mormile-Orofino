package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Rules.Rules;
import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.util.exception.SpecificCloudNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameModelTest {
    GameModel gameModel;

    @BeforeEach
    public void setUp() {
        gameModel = new GameModel();
    }


    @AfterEach
    public void tearDown() {
        gameModel = null;
    }


    @Test
    public void getPlayerHandlerTest(){
        assertEquals(true,gameModel.getPlayerHandler() instanceof PlayersHandler);
    }


    @Test
    public void getBagTest(){
        assertEquals(true,gameModel.getStudentsBag() instanceof Bag);
    }


    @Test
    public void getTeacherHandlerTest(){
        assertEquals(true,gameModel.getTeacherHandler() instanceof TeachersHandler);
    }

    @Test
    public void getIslandHandlerTest(){
        assertEquals(true,gameModel.getIslandHandler() instanceof IslandsHandler);
    }


    @Test
    public void getCloudsTest(){

        gameModel.getPlayerHandler().addPlayer(new Player("id_p1"));
        gameModel.getPlayerHandler().addPlayer(new Player("id_p2"));
        gameModel.getPlayerHandler().addPlayer(new Player("id_p3"));

        Cloud cloud= new Cloud(0);
        try {
            gameModel.addCloud(cloud);
        } catch (SpecificCloudNotFoundException ignored) {

        }
        assertEquals(true,gameModel.getClouds().contains(cloud));
        assertEquals(cloud,gameModel.getClouds().get(0));
        try {
            assertEquals(cloud,gameModel.getCloudByID(0));
        } catch (SpecificCloudNotFoundException ignored) {

        }

        Cloud cloud2= new Cloud(1);
        try {
            gameModel.addCloud(cloud2);
        } catch (SpecificCloudNotFoundException ignored) {

        }
        assertEquals(true,gameModel.getClouds().contains(cloud2));
        assertEquals(cloud2,gameModel.getClouds().get(1));
        try {
            assertEquals(cloud2,gameModel.getCloudByID(1));
        } catch (SpecificCloudNotFoundException ignored) {

        }

        Cloud cloud3= new Cloud(2);
        try {
            gameModel.addCloud(cloud3);
        } catch (SpecificCloudNotFoundException ignored) {

        }
        assertEquals(true,gameModel.getClouds().contains(cloud3));
        assertEquals(cloud3,gameModel.getClouds().get(2));
        try {
            assertEquals(cloud3,gameModel.getCloudByID(2));
        } catch (SpecificCloudNotFoundException ignored) {

        }

        Cloud cloud4= new Cloud(3);
        try {
            gameModel.addCloud(cloud3);
        } catch (SpecificCloudNotFoundException ignored) {

        }
        assertEquals(false,gameModel.getClouds().contains(cloud4));
        assertEquals(3,gameModel.getClouds().size());
        try {
            assertEquals(cloud4,gameModel.getCloudByID(3));
        } catch (SpecificCloudNotFoundException ignored) {

        }


    }

    @Test
    public void coinTest(){
        for(int i = 0; i < 20; i++){
            assertEquals(true,gameModel.coinsAreEnough());
            gameModel.getCoin();
        }
        assertEquals(false, gameModel.coinsAreEnough());

    }

    @Test
    public void characterTest(){
        Character c = new Character("n1","des1",1,1,new Rules());
        gameModel.addCharacter(c);
        try {
            assertEquals(c,gameModel.getCharacterFromID(1));
        } catch (SpecificCharacterNotFoundException ignored) {

        }
        try {
            assertEquals(c,gameModel.getCharacterFromID(2));
        } catch (SpecificCharacterNotFoundException ignored) {

        }
        Character c2 = new Character("n2","des2",1,2,new Rules());
        gameModel.addCharacter(c2);
        try {
            assertEquals(c2,gameModel.getCharacterFromID(2));
        } catch (SpecificCharacterNotFoundException ignored) {

        }
        assertEquals(true, gameModel.getCharacters().contains(c));
        assertEquals(true, gameModel.getCharacters().contains(c2));



    }



}
