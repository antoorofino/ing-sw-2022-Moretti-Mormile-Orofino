package it.polimi.ingsw.server.rules;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.*;
import it.polimi.ingsw.util.exception.CardException;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RulesTest {
	Rules rules;
	GameModel game;
	Player firstPlayer,secondPlayer;

	@BeforeEach
	public void setUp() {
		game = new GameModel(new GameListInfo("test", GameMode.BASIC,2));
		rules = new Rules(game);

		// setup player
		PlayersHandler ph = game.getPlayerHandler();
		Player p1 = new Player("id_p1");
		Player p2 = new Player("id_p2");
		ph.addPlayer(p1);
		ph.addPlayer(p2);

		// setup game
		game.setupGameTest();

		//  Planning phase
		//  firstPlayer -> card 3, secondPlayer -> card 6
		ph.initialiseCurrentPlayerPlanningPhase();
		firstPlayer = ph.getCurrentPlayer();
		try {
			ph.getCurrentPlayer().setLastCardUsed(ph.getCurrentPlayer().getDeck().get(2));
		} catch (CardException e){
			fail();
		}

		ph.nextPlayerByOrder();
		secondPlayer = ph.getCurrentPlayer();
		try {
			ph.getCurrentPlayer().setLastCardUsed(ph.getCurrentPlayer().getDeck().get(5));
		} catch (CardException e){
			fail();
		}

		//      Action phase
		//      firstPlayer, secondPlayer
		ph.initialiseCurrentPlayerActionPhase();
	}


	@AfterEach
	public void tearDown() {
		rules = null;
		game = null;
	}

	@Test
	public void doActionTest()  {
		// -------------- Action MOVE_STUDENT_TO_ISLAND ----------------- //
		// student not present at the entrance
		assertFalse(rules.doAction(new Action(ActionType.MOVE_STUDENT_TO_ISLAND,Piece.UNICORN,0)));
		// invalid island id
		assertFalse(rules.doAction(new Action(ActionType.MOVE_STUDENT_TO_ISLAND,Piece.DRAGON,12)));

		// frog from entrance to first island
		assertTrue(rules.doAction(new Action(ActionType.MOVE_STUDENT_TO_ISLAND,Piece.FROG,0)));
		assertFalse(firstPlayer.getPlayerBoard().getStudentsEntrance().contains(Piece.FROG));
		try {
			assertEquals(1, game.getIslandHandler().getIslandByID(0).getNumStudents(Piece.FROG));
		} catch (SpecificIslandNotFoundException ignored) {
			fail();
		}
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_ISLAND));

		// -------------- Action MOVE_STUDENT_TO_DININGROOM ----------------- //
		// student not present at the entrance
		assertFalse(rules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM,Piece.UNICORN)));

		// move dragon from entrance to dining
		assertTrue(rules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM,Piece.DRAGON)));
		assertEquals(1,firstPlayer.getPlayerBoard().getNumOfStudentsRoom(Piece.DRAGON));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));


		// -------------- Action MOVE_MOTHER_NATURE ----------------- //
		// move mother nature too soon
		assertFalse(rules.doAction(new Action(ActionType.MOVE_MOTHER_NATURE,1)));
		rules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM,Piece.DRAGON));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));

		// too many steps
		assertFalse(rules.doAction(new Action(ActionType.MOVE_MOTHER_NATURE,6)));

		// move mother
		assertTrue(rules.doAction(new Action(ActionType.MOVE_MOTHER_NATURE,1)));
		assertEquals(1,game.getIslandHandler().getMotherNature());
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_MOTHER_NATURE));

		// -------------- Action CHOOSE_CLOUD ----------------- //
		// invalid cloud id
		assertFalse(rules.doAction(new Action(ActionType.CHOOSE_CLOUD,3)));

		// choose cloud
		assertTrue(rules.doAction(new Action(ActionType.CHOOSE_CLOUD,1)));

		// empty cloud
		assertFalse(rules.doAction(new Action(ActionType.CHOOSE_CLOUD,1)));

	}
}
