package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.PlayerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class PlayersHandlerTest {
    PlayersHandler playerHandler;

    @BeforeEach
    public void setUp() {
        playerHandler = new PlayersHandler();
    }


    @AfterEach
    public void tearDown() {
        playerHandler = null;
    }

    @Test
    public void playerTest(){
        Player p1 = new Player("id_p1");
        ArrayList<String>nicknames = new ArrayList<>();
        p1.setNickname("Moretti");
        Player p2 = new Player("is_p2");
        p2.setNickname("Mormile");
        Player p3 = new Player("id_p3");
        p3.setNickname("Orofino");
        playerHandler.addPlayer(p1);
        playerHandler.addPlayer(p2);
        playerHandler.addPlayer(p3);
        nicknames.add("Moretti");
        nicknames.add("Mormile");
        nicknames.add("Orofino");
        try {
            assertEquals(p1,playerHandler.getPlayersByNickName("Moretti"));
        } catch (PlayerException e) {
            e.printStackTrace();
        }

        try {
            playerHandler.getPlayersByNickName("Ciccio");
        } catch (PlayerException ignored) {
        }

        ArrayList<String>nicknames2;
        nicknames2 = playerHandler.getPlayersNickName();
        assertEquals(nicknames.size(),nicknames2.size());
        for(int i = 0; i<nicknames.size(); i++){
            assertEquals(nicknames.get(i),nicknames2.get(i));
        }

        ArrayList<Player> players = playerHandler.getPlayers();
        assertEquals(players.get(0),p1);
        assertEquals(players.get(1),p2);
        assertEquals(players.get(2),p3);

    }

    //TODO metodi del controller e get current player
}
