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

import java.util.Arrays;
import java.util.Collections;

public class AssistanceCardCheckTest {

    GameModel game;
    GameController controller;
    Player p1, p2;

    @BeforeEach
    public void setUp() {
        controller = new GameController(new GameListInfo("test", GameMode.BASIC, 2), new Logger(0));
        game = controller.getGame();

        // setup player
        PlayersHandler ph = game.getPlayerHandler();
        p1 = new Player("id_p1");
        p2 = new Player("id_p2");
        game.getPlayerHandler().addPlayer(p1);
        game.getPlayerHandler().addPlayer(p2);

        game.getPlayerHandler().initialiseCurrentPlayerPlanningPhase();
    }

    @AfterEach
    public void tearDown() {
        game = null;
        controller = null;
    }

    @Test
    public void assistantCardCanBePlayed() throws CardException {
        /*
            Check if an assistant card can be played
         */
        AssistantCard c1 = new AssistantCard(1,1,1);
        AssistantCard c2 = new AssistantCard(2,2,2);

        p1.addCards(Arrays.asList(c1, c2));
        p2.addCards(Arrays.asList(c1, c2));

        Assertions.assertTrue(controller.checkAssistantCard(c1));
        Assertions.assertTrue(controller.checkAssistantCard(c2));

        game.getPlayerHandler().getCurrentPlayer().setLastCardUsed(c1);
        game.getPlayerHandler().nextPlayerByOrder();

        Assertions.assertFalse(controller.checkAssistantCard(c1));
        Assertions.assertTrue(controller.checkAssistantCard(c2));
    }

    @Test
    public void assistantCardDuplicateAllowed() throws CardException {
        /*
            Check if an assistant card with the same value can be played
         */
        AssistantCard c1 = new AssistantCard(1,1,1);
        AssistantCard c2 = new AssistantCard(2,2,2);

        p1.addCards(Arrays.asList(c1, c2));
        p2.addCards(Collections.singletonList(c1));

        Assertions.assertTrue(controller.checkAssistantCard(c1));
        Assertions.assertTrue(controller.checkAssistantCard(c2));

        game.getPlayerHandler().getCurrentPlayer().setLastCardUsed(c1);
        game.getPlayerHandler().nextPlayerByOrder();

        Assertions.assertTrue(controller.checkAssistantCard(c1));
    }
}