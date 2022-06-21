package it.polimi.ingsw.model;

import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {
    GameModel gameModel;

    @AfterEach
    public void tearDown() {
        gameModel = null;
    }

    @Test
    public void setUpGame2PlayersTest(){
        // create game
        GameListInfo listInfo = new GameListInfo("test",GameMode.EXPERT,2);
        gameModel = new GameModel(listInfo);
        Player p1 = new Player("id_01");
        Player p2 = new Player("id_02");
        gameModel.getPlayerHandler().addPlayer(p1);
        gameModel.getPlayerHandler().addPlayer(p2);
        gameModel.setupGame();
        // check game handlers
        assertNotNull(gameModel.getPlayerHandler());
        assertNotNull(gameModel.getStudentsBag());
        assertNotNull(gameModel.getTeacherHandler());
        assertNotNull(gameModel.getIslandHandler());
        // check game mode
        assertEquals(GameMode.EXPERT,gameModel.getGameMode());
        // check game name
        assertEquals("test",gameModel.getGameName());
        //check empty clouds
        assertEquals(2,gameModel.getClouds().size());
        for (Cloud cloud: gameModel.getClouds()) {
            assertEquals(0,cloud.getStudents().size());
        }
        // check refill clouds
        gameModel.cloudsRefill();
        for (Cloud cloud: gameModel.getClouds()) {
            assertEquals(3,cloud.getStudents().size());
        }
        // check num coins
        for(int i = 0; i < 18; i++){
            assertTrue(gameModel.coinsAreEnough());
            gameModel.getCoin();
        }
        assertFalse(gameModel.coinsAreEnough());
        // check characters
        assertEquals(3,gameModel.getCharacters().size());
    }

    @Test
    public void setUpGame3PlayersTest(){
        // create game
        GameListInfo listInfo = new GameListInfo("test",GameMode.EXPERT,3);
        gameModel = new GameModel(listInfo);
        Player p1 = new Player("id_01");
        Player p2 = new Player("id_02");
        Player p3 = new Player("id_03");
        gameModel.getPlayerHandler().addPlayer(p1);
        gameModel.getPlayerHandler().addPlayer(p2);
        gameModel.getPlayerHandler().addPlayer(p3);
        gameModel.setupGame();
        // check game handlers
        assertNotNull(gameModel.getPlayerHandler());
        assertNotNull(gameModel.getStudentsBag());
        assertNotNull(gameModel.getTeacherHandler());
        assertNotNull(gameModel.getIslandHandler());
        // check game mode
        assertEquals(GameMode.EXPERT,gameModel.getGameMode());
        // check game name
        assertEquals("test",gameModel.getGameName());
        //check clouds
        assertEquals(3,gameModel.getClouds().size());
        for (Cloud cloud: gameModel.getClouds()) {
            assertEquals(0,cloud.getStudents().size());
        }
    }
}
