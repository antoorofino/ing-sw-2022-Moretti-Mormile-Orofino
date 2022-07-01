package it.polimi.ingsw.server;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayersHandler;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.Logger;
import it.polimi.ingsw.util.exception.CardException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AssistanceCardsTest {

    GameModel game;
    GameController controller;

    @BeforeEach
    public void setUp() {
        controller = new GameController(new GameListInfo("test", GameMode.BASIC, 2), new Logger(0));
        game = controller.getGame();

        // setup player
        PlayersHandler ph = game.getPlayerHandler();
        Player p1 = new Player("id_p1");
        Player p2 = new Player("is_p2");
        ph.addPlayer(p1);
        ph.addPlayer(p2);

        List<AssistantCard> cards = new ArrayList<>();
        cards.add(new AssistantCard(1,1,1));
        cards.add(new AssistantCard(2,2,2));
        p1.addCards(cards);
        p2.addCards(cards);

        game.getPlayerHandler().initialiseCurrentPlayerPlanningPhase();
    }

    @AfterEach
    public void tearDown() {
        game = null;
        controller = null;
    }

    @Test
    public void cardsTest() throws CardException {
        Assertions.assertTrue(controller.checkAssistantCard(new AssistantCard(1,1,1)));

        game.getPlayerHandler().getCurrentPlayer().setLastCardUsed(new AssistantCard(1,1,1));
        game.getPlayerHandler().nextPlayerByOrder();

        Assertions.assertFalse(controller.checkAssistantCard(new AssistantCard(1,1,1)));

        game.getPlayerHandler().getCurrentPlayer().setLastCardUsed(new AssistantCard(2,2,2));
        Assertions.assertTrue(controller.checkAssistantCard(new AssistantCard(1,1,1)));
    }
}