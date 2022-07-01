package it.polimi.ingsw.model;

import it.polimi.ingsw.util.TowerColor;
import it.polimi.ingsw.util.exception.CardException;
import it.polimi.ingsw.util.exception.PlayerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PlayersHandlerTest {
    PlayersHandler playersHandler;
    Player p1, p2, p3;

    @BeforeEach
    public void setUp() {
        playersHandler = new PlayersHandler(3);
        assertEquals(3, playersHandler.getNumPlayers());
        p1 = new Player("id_p1");
        p2 = new Player("id_p2");
        p3 = new Player("id_p3");
        playersHandler.addPlayer(p1);
        playersHandler.addPlayer(p2);
        playersHandler.addPlayer(p3);
    }

    @AfterEach
    public void tearDown() {
        playersHandler = null;
    }

    @Test
    public void getPlayerByIdTest(){
        try {
            assertEquals(p1, playersHandler.getPlayerById("id_p1"));
        } catch (PlayerException e) {
            fail();
        }
        // throws exception
        assertThrows(PlayerException.class,() -> {
            playersHandler.getPlayerById("id_p4");
        });
    }
    @Test
    public void getPlayersByNickNameTest(){
        p1.setNickname("Moretti");
        p2.setNickname("Mormile");
        p3.setNickname("Orofino");

        ArrayList<String>nicknames = new ArrayList<>();
        nicknames.add("Moretti");
        nicknames.add("Mormile");
        nicknames.add("Orofino");
        try {
            assertEquals(p1, playersHandler.getPlayersByNickName("Moretti"));
        } catch (PlayerException e) {
            fail();
        }
        // throws exception
        assertThrows(PlayerException.class,() -> {
            playersHandler.getPlayersByNickName("Ciccio");
        });

        ArrayList<String> nicknames2;
        nicknames2 = playersHandler.getPlayersNickName();
        assertEquals(nicknames.size(),nicknames2.size());
        for(int i = 0; i < nicknames.size(); i++){
            assertEquals(nicknames.get(i),nicknames2.get(i));
        }

        List<Player> players = playersHandler.getPlayers();
        assertEquals(players.get(0),p1);
        assertEquals(players.get(1),p2);
        assertEquals(players.get(2),p3);
    }

    @Test
    public void everyPlayerIsReadyToPlayTest(){
        // to be ready need nickname and tower color
        p1.setNickname("Moretti");
        p2.setNickname("Mormile");
        p3.setNickname("Orofino");
        assertFalse(playersHandler.everyPlayerIsReadyToPlay());
        p1.setTowerColor(TowerColor.BLACK);
        p2.setTowerColor(TowerColor.WHITE);
        p3.setTowerColor(TowerColor.GRAY);
        assertTrue(playersHandler.everyPlayerIsReadyToPlay());
    }

    @Test
    public void addPlayerTest(){
        Player p4 = new Player("id_p4");
        // throws exception
        assertThrows(IllegalStateException.class,() -> {
            playersHandler.addPlayer(p4);
        });
    }

    @Test
    public void playersOrderTest(){
        p1.addCards(AssistantCard.createDeck());
        p2.addCards(AssistantCard.createDeck());
        p3.addCards(AssistantCard.createDeck());

        assertNull(playersHandler.getCurrentPlayer());
        playersHandler.initialiseCurrentPlayerPlanningPhase();
        assertEquals(0, playersHandler.cardsAlreadyPlayed().size());

        //----------------------------------------------------------------
        //                          FIRST ROUND
        //  Planning phase
        //      firstPlayer -> card 3, secondPlayer -> card 3, thirdPlayer -> card 1

        Player firstPlayer = playersHandler.getCurrentPlayer();
        try {
            playersHandler.getCurrentPlayer().setLastCardUsed(playersHandler.getCurrentPlayer().getDeck().get(2));
        } catch (CardException e){
            fail();
        }
        playersHandler.nextPlayerByOrder();
        assertEquals(1, playersHandler.cardsAlreadyPlayed().size());

        Player secondPlayer = playersHandler.getCurrentPlayer();
        try {
            playersHandler.getCurrentPlayer().setLastCardUsed(playersHandler.getCurrentPlayer().getDeck().get(2));
        } catch (CardException e){
            fail();
        }
        playersHandler.nextPlayerByOrder();
        assertEquals(2, playersHandler.cardsAlreadyPlayed().size());

        Player thirdPlayer = playersHandler.getCurrentPlayer();
        try {
            playersHandler.getCurrentPlayer().setLastCardUsed(playersHandler.getCurrentPlayer().getDeck().get(0));
        } catch (CardException e){
            fail();
        }
        playersHandler.nextPlayerByOrder();
        assertEquals(3, playersHandler.cardsAlreadyPlayed().size());

        //  Action phase
        //      thirdPlayer, firstPlayer, secondPlayer
        playersHandler.initialiseCurrentPlayerActionPhase();
        assertEquals(thirdPlayer, playersHandler.getCurrentPlayer());
        playersHandler.nextPlayerByAssistance();
        assertEquals(firstPlayer, playersHandler.getCurrentPlayer());
        playersHandler.nextPlayerByAssistance();
        assertEquals(secondPlayer, playersHandler.getCurrentPlayer());
        playersHandler.nextPlayerByAssistance();
        assertEquals(secondPlayer, playersHandler.getCurrentPlayer());

        //----------------------------------------------------------------
        //                          SECOND ROUND
        //  Planning phase
        //      thirdPlayer -> card 5, firstPlayer -> card 1, secondPlayer -> card 5
        playersHandler.initialiseCurrentPlayerPlanningPhase();
        assertEquals(0, playersHandler.cardsAlreadyPlayed().size());

        assertEquals(thirdPlayer, playersHandler.getCurrentPlayer());
        try {
            playersHandler.getCurrentPlayer().setLastCardUsed(playersHandler.getCurrentPlayer().getDeck().get(4));
        } catch (CardException e){
            fail();
        }
        playersHandler.nextPlayerByOrder();
        assertEquals(1, playersHandler.cardsAlreadyPlayed().size());
        assertEquals(firstPlayer, playersHandler.getCurrentPlayer());
        try {
            playersHandler.getCurrentPlayer().setLastCardUsed(playersHandler.getCurrentPlayer().getDeck().get(0));
        } catch (CardException e){
            fail();
        }
        playersHandler.nextPlayerByOrder();
        assertEquals(2, playersHandler.cardsAlreadyPlayed().size());
        assertEquals(secondPlayer, playersHandler.getCurrentPlayer());
        try {
            playersHandler.getCurrentPlayer().setLastCardUsed(playersHandler.getCurrentPlayer().getDeck().get(4));
        } catch (CardException e){
            fail();
        }
        playersHandler.nextPlayerByOrder();
        assertEquals(3, playersHandler.cardsAlreadyPlayed().size());
        assertEquals(thirdPlayer, playersHandler.getCurrentPlayer());

        //  Action phase
        //      firstPlayer, thirdPlayer, secondPlayer
        playersHandler.initialiseCurrentPlayerActionPhase();
        assertEquals(firstPlayer, playersHandler.getCurrentPlayer());
        playersHandler.nextPlayerByAssistance();
        assertEquals(thirdPlayer, playersHandler.getCurrentPlayer());
        playersHandler.nextPlayerByAssistance();
        assertEquals(secondPlayer, playersHandler.getCurrentPlayer());
        playersHandler.nextPlayerByAssistance();
        assertEquals(secondPlayer, playersHandler.getCurrentPlayer());
    }

    @Test
    public void playerNoMoreCardsTest(){
        p1.addCards(AssistantCard.createDeck());
        p2.addCards(AssistantCard.createDeck());
        p3.addCards(AssistantCard.createDeck());
        for(int i = 0;  i < 10; i++){
            assertFalse(playersHandler.playerWithNoMoreCards());
            try {
                p1.setLastCardUsed(p1.getDeck().get(0));
            } catch (CardException e){
                fail();
            }
        }
        assertTrue(playersHandler.playerWithNoMoreCards());
    }
}
