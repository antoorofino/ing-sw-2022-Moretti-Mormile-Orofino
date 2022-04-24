package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.Exception.SpecificCloudNotFoundException;
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

        gameModel.getPlayerHandler().addPlayer(new Player());
        gameModel.getPlayerHandler().addPlayer(new Player());
        gameModel.getPlayerHandler().addPlayer(new Player());

        Cloud cloud= new Cloud(0);
        try {
            gameModel.addCloud(cloud);
        } catch (SpecificCloudNotFoundException e) {

        }
        assertEquals(true,gameModel.getClouds().contains(cloud));
        assertEquals(cloud,gameModel.getClouds().get(0));
        try {
            assertEquals(cloud,gameModel.getCloudByID(0));
        } catch (SpecificCloudNotFoundException e) {

        }

        Cloud cloud2= new Cloud(1);
        try {
            gameModel.addCloud(cloud2);
        } catch (SpecificCloudNotFoundException e) {

        }
        assertEquals(true,gameModel.getClouds().contains(cloud2));
        assertEquals(cloud2,gameModel.getClouds().get(1));
        try {
            assertEquals(cloud2,gameModel.getCloudByID(1));
        } catch (SpecificCloudNotFoundException e) {

        }

        Cloud cloud3= new Cloud(2);
        try {
            gameModel.addCloud(cloud3);
        } catch (SpecificCloudNotFoundException e) {

        }
        assertEquals(true,gameModel.getClouds().contains(cloud3));
        assertEquals(cloud3,gameModel.getClouds().get(2));
        try {
            assertEquals(cloud3,gameModel.getCloudByID(2));
        } catch (SpecificCloudNotFoundException e) {

        }

        Cloud cloud4= new Cloud(3);
        try {
            gameModel.addCloud(cloud3);
        } catch (SpecificCloudNotFoundException e) {

        }
        assertEquals(false,gameModel.getClouds().contains(cloud4));
        assertEquals(3,gameModel.getClouds().size());
        try {
            assertEquals(cloud4,gameModel.getCloudByID(3));
        } catch (SpecificCloudNotFoundException e) {

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
        } catch (SpecificCharacterNotFoundException e) {

        }
        try {
            assertEquals(c,gameModel.getCharacterFromID(2));
        } catch (SpecificCharacterNotFoundException e) {

        }
        Character c2 = new Character("n2","des2",1,2,new Rules());
        gameModel.addCharacter(c2);
        try {
            assertEquals(c2,gameModel.getCharacterFromID(2));
        } catch (SpecificCharacterNotFoundException e) {

        }
        assertEquals(true, gameModel.getCharacters().contains(c));
        assertEquals(true, gameModel.getCharacters().contains(c2));



    }



}
