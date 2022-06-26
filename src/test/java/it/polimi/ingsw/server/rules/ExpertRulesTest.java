package it.polimi.ingsw.server.rules;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Piece;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayersHandler;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.util.exception.CardException;
import it.polimi.ingsw.util.exception.SpecificCharacterNotFoundException;
import it.polimi.ingsw.util.exception.SpecificIslandNotFoundException;
import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ExpertRulesTest {
	ExpertRules expertRules;
	GameModel game;
	Player firstPlayer, secondPlayer;

	@BeforeEach
	public void setUp() {
		game = new GameModel(new GameListInfo("test", GameMode.EXPERT, 2));
		expertRules = new ExpertRules(game);

		// setup player
		PlayersHandler ph = game.getPlayerHandler();
		Player p1 = new Player("id_p1");
		Player p2 = new Player("is_p2");
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
		} catch (CardException e) {
			fail();
		}

		ph.nextPlayerByOrder();
		secondPlayer = ph.getCurrentPlayer();
		try {
			ph.getCurrentPlayer().setLastCardUsed(ph.getCurrentPlayer().getDeck().get(5));
		} catch (CardException e) {
			fail();
		}

		//      Action phase
		//      firstPlayer, secondPlayer
		ph.initialiseCurrentPlayerActionPhase();
	}


	@AfterEach
	public void tearDown() {
		expertRules = null;
		game = null;
	}

	@Test
	public void doActionBasicTest() {
		// -------------- Action MOVE_STUDENT_TO_DININGROOM (Basic Action) ----------------- //
		assertTrue(expertRules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
		assertEquals(1, firstPlayer.getPlayerBoard().getNumOfStudentsRoom(Piece.DRAGON));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));

		// invalid action (no unicorn in entrance)
		assertFalse(expertRules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.UNICORN)));

		// x3 to get a coin
		assertTrue(expertRules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		assertTrue(expertRules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		assertEquals(2, firstPlayer.getCoin());

		// move mother
		assertTrue(expertRules.doAction(new Action(ActionType.MOVE_MOTHER_NATURE,2)));

		// not valid id character
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,0)));

	}

	@Test
	public void doActionChooseFirst() {
		// ------------ first character (activation needed) --------------- //
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER, 1)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check activation needed
		assertEquals(ActionType.STUDENT_FROM_CARD_TO_ISLAND, firstPlayer.getActiveCharacter().getRules()
				.nextPossibleActions().getActionsList().get(0).getActionType());
		// invalid activation
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.FROG)));
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ISLAND, Piece.FROG, 0)));
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ISLAND, Piece.DRAGON,12)));

		// valid activation (get dragon from character)
		Integer numDragon = 0;
		try {
			numDragon = game.getIslandHandler().getIslandByID(0).getNumStudents(Piece.DRAGON);
		} catch (SpecificIslandNotFoundException e) {
			fail();
		}
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ISLAND, Piece.DRAGON, 0)));
		try {
			assertEquals(numDragon + 1, game.getIslandHandler().getIslandByID(0).getNumStudents(Piece.DRAGON));
		} catch (SpecificIslandNotFoundException e) {
			fail();
		}
		// check auto refill card
		assertEquals(4, firstPlayer.getActiveCharacter().getStudents().size());
	}

	@Test
	public void doActionChooseSecond() {
		// ------------ second character (no activation needed) --------------- //
		firstPlayer.setCoin(2);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,2)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check no activation needed
		assertFalse(firstPlayer.getActiveCharacter().getRules().nextPossibleActions().getActionsList()
				.stream().map(Action::getActionType).collect(Collectors.toList())
						.contains(ActionType.ACTIVATED_CHARACTER));
		assertTrue(firstPlayer.getRoundActions().getActionsList().stream()
				.map(Action::getActionType).collect(Collectors.toList())
				.contains(ActionType.ACTIVATED_CHARACTER));
		// move students to invoke calculateInfluence
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
	}

	@Test
	public void doActionChooseThird() {
		// ------------ third character (activation needed) --------------- //
		firstPlayer.setCoin(3);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER, 3)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check activation needed
		assertEquals(ActionType.DOUBLE_INFLUENCE, firstPlayer.getActiveCharacter().getRules()
				.nextPossibleActions().getActionsList().get(0).getActionType());
		// invalid activation
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.FROG)));
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.DOUBLE_INFLUENCE, 12)));

		// no entry (valid activation)
		try {
			game.getIslandHandler().getIslandByID(1).addFlagNoInfluence();
		} catch (SpecificIslandNotFoundException e) {
			fail();
		}
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.DOUBLE_INFLUENCE,1)));
	}

	@Test
	public void doActionChooseFourth() {
		// ------------ fourth character (no activation needed but before move mother) --------------- //
		// chose character after move mother
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_MOTHER_NATURE));
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER, 4)));

		// chose character before move mother
		firstPlayer.resetRoundAction();
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,4)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check no activation needed
		assertFalse(firstPlayer.getActiveCharacter().getRules().nextPossibleActions().getActionsList()
				.stream().map(Action::getActionType).collect(Collectors.toList())
				.contains(ActionType.ACTIVATED_CHARACTER));
		assertTrue(firstPlayer.getRoundActions().getActionsList().stream()
				.map(Action::getActionType).collect(Collectors.toList())
				.contains(ActionType.ACTIVATED_CHARACTER));
		// move x 3 students to move mother
		for(int i = 0;i < 3;i++){
			assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
			game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		}

		//invalid moves
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_MOTHER_NATURE,5)));

		//valid moves
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_MOTHER_NATURE,4)));
	}

	@Test
	public void doActionChooseFifth() {
		// ------------ fifth character (activation needed) --------------- //
		firstPlayer.setCoin(2);
		// not flags on card
		try {
			game.getCharacterFromID(5).setIslandFlag(0);
		} catch (SpecificCharacterNotFoundException e) {
			fail();
		}
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER, 5)));

		// restore flag
		try {
			game.getCharacterFromID(5).setIslandFlag(4);
		} catch (SpecificCharacterNotFoundException e) {
			fail();
		}
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,5)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check activation needed
		assertEquals(ActionType.NO_INFLUENCE, firstPlayer.getActiveCharacter().getRules()
				.nextPossibleActions().getActionsList().get(0).getActionType());
		// invalid activation
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.NO_INFLUENCE,12)));

		// valid activation
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.NO_INFLUENCE,1)));

		try {
			assertEquals(1,game.getIslandHandler().getIslandByID(1).getFlagNoInfluence());
		} catch (SpecificIslandNotFoundException e) {
			fail();
		}

		// move x 3 students to move mother and remove flag
		for(int i = 0;i < 3;i++){
			assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
			game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		}

		//valid moves
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_MOTHER_NATURE,1)));

		try {
			assertEquals(0,game.getIslandHandler().getIslandByID(1).getFlagNoInfluence());
		} catch (SpecificIslandNotFoundException e) {
			fail();
		}
	}

	@Test
	public void doActionChooseSixth() {
		// ------------ sixth character (no activation needed) --------------- //
		// non enough coins
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,6)));
		firstPlayer.setCoin(3);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,6)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check no activation needed
		assertFalse(firstPlayer.getActiveCharacter().getRules().nextPossibleActions().getActionsList()
				.stream().map(Action::getActionType).collect(Collectors.toList())
				.contains(ActionType.ACTIVATED_CHARACTER));
		assertTrue(firstPlayer.getRoundActions().getActionsList().stream()
				.map(Action::getActionType).collect(Collectors.toList())
				.contains(ActionType.ACTIVATED_CHARACTER));
		// move x 3 students to move mother
		for(int i = 0;i < 3;i++){
			assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
			game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		}
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_MOTHER_NATURE,2)));
	}

	@Test
	public void doActionChooseSeventh() {
		// ------------ sevent character (activation needed) --------------- //
		// empty entrance
		for (Piece p:firstPlayer.getPlayerBoard().getStudentsEntrance()) {
			try {
				firstPlayer.getPlayerBoard().removeFromEntrance(p);
			} catch (SpecificStudentNotFoundException e) {
				fail();
			}
		}
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,7)));

		// restore entrance
		firstPlayer.getPlayerBoard().addToEntrance(Arrays.asList(Piece.DRAGON,Piece.DRAGON,Piece.DRAGON,Piece.FROG,Piece.GNOME));
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,7)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check activation needed
		assertEquals(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, firstPlayer.getActiveCharacter().getRules()
				.nextPossibleActions().getActionsList().get(0).getActionType());
		// invalid activation
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, null, null)));
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, Piece.FROG,  Piece.DRAGON)));
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, Piece.DRAGON, Piece.UNICORN)));

		// valid activation
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, Piece.DRAGON,  Piece.DRAGON)));
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, Piece.DRAGON,  Piece.DRAGON)));
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, Piece.DRAGON,  Piece.DRAGON)));
		// max 3 changes
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, Piece.DRAGON,  Piece.DRAGON)));

		// stop before 3 changes
		firstPlayer.resetRoundAction();
		firstPlayer.setCoin(1);
		// not enough coins
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER, 7)));
		firstPlayer.setCoin(2);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,7)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));

		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, Piece.DRAGON,  Piece.DRAGON)));
		// stop
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, -1)));
		// stopped character actions
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_ENTRANCE, Piece.DRAGON,  Piece.DRAGON)));
	}

	@Test
	public void doActionChooseEighth() {
		// ------------ eighth character (no activation needed) --------------- //
		firstPlayer.setCoin(2);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,8)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check no activation needed
		assertFalse(firstPlayer.getActiveCharacter().getRules().nextPossibleActions().getActionsList()
				.stream().map(Action::getActionType).collect(Collectors.toList())
				.contains(ActionType.ACTIVATED_CHARACTER));
		assertTrue(firstPlayer.getRoundActions().getActionsList().stream()
				.map(Action::getActionType).collect(Collectors.toList())
				.contains(ActionType.ACTIVATED_CHARACTER));
		// move x 3 students to move mother
		for(int i = 0;i < 3;i++){
			assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
			game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		}

		//valid moves
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_MOTHER_NATURE,2)));
	}

	@Test
	public void doActionChooseNinth() {
		// ------------ ninth character (activation needed) --------------- //
		firstPlayer.setCoin(3);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,9)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check activation needed
		assertEquals(ActionType.COLOR_NO_INFLUENCE, firstPlayer.getActiveCharacter().getRules()
				.nextPossibleActions().getActionsList().get(0).getActionType());
		// invalid activation
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.COLOR_NO_INFLUENCE, null)));

		// valid activation
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.COLOR_NO_INFLUENCE, Piece.DRAGON)));
		// move x 3 students to move mother
		for(int i = 0;i < 3;i++){
			assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
			game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		}

		//valid moves
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_MOTHER_NATURE,2)));
	}

	@Test
	public void doActionChooseTenth() {
		// ------------ tenth character (activation needed) --------------- //
		// invalid activation (no students in dining room)
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,10)));
		// move student to dining room
		assertTrue(expertRules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		// invalid activation (empty entrance)
		for (Piece p:firstPlayer.getPlayerBoard().getStudentsEntrance()) {
			try {
				firstPlayer.getPlayerBoard().removeFromEntrance(p);
			} catch (SpecificStudentNotFoundException e) {
				fail();
			}
		}
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,10)));
		// restore entrance
		firstPlayer.getPlayerBoard().addToEntrance(Arrays.asList(Piece.DRAGON,Piece.DRAGON,Piece.DRAGON,Piece.FROG,Piece.GNOME));

		// valid activation
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,10)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check activation needed
		assertEquals(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, firstPlayer.getActiveCharacter().getRules()
				.nextPossibleActions().getActionsList().get(0).getActionType());
		// invalid activation (no students in dining room)
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, null, null)));
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, Piece.DRAGON,  Piece.FROG)));
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, Piece.UNICORN, Piece.DRAGON)));

		// valid activation
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, Piece.DRAGON,  Piece.DRAGON)));
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, Piece.DRAGON,  Piece.DRAGON)));
		// max 2 changes
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, Piece.DRAGON,  Piece.DRAGON)));

		// stop before 2 changes
		firstPlayer.resetRoundAction();
		firstPlayer.setCoin(1);
		// not enough coins
		assertFalse(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER, 10)));
		firstPlayer.setCoin(2);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,10)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));

		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, Piece.DRAGON,  Piece.DRAGON)));
		// stop
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, -1)));
		// stopped character actions
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_ENTRANCE_TO_DINING, Piece.DRAGON,  Piece.DRAGON)));
	}

	@Test
	public void doActionChooseEleventh() {
		// ------------ eleventh character (activation needed) --------------- //
		firstPlayer.setCoin(2);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,11)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check activation needed
		assertEquals(ActionType.STUDENT_FROM_CARD_TO_DINING, firstPlayer.getActiveCharacter().getRules()
				.nextPossibleActions().getActionsList().get(0).getActionType());
		// invalid activation
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_DINING, Piece.FROG)));

		// valid activation (get dragon from character)
		Integer numDragon = 0;
		numDragon = firstPlayer.getPlayerBoard().getNumOfStudentsRoom(Piece.DRAGON);
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_CARD_TO_DINING, Piece.DRAGON)));
		assertEquals(numDragon + 1, firstPlayer.getPlayerBoard().getNumOfStudentsRoom(Piece.DRAGON));

		// check auto refill card
		assertEquals(4, firstPlayer.getActiveCharacter().getStudents().size());
	}

	@Test
	public void doActionChooseTwelfth() {
		// ------------ twelfth character (activation needed) --------------- //
		// move dragon to dining
		Integer numDragon = 0;
		assertTrue(expertRules.doAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM, Piece.DRAGON)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.MOVE_STUDENT_TO_DININGROOM));
		numDragon = firstPlayer.getPlayerBoard().getNumOfStudentsRoom(Piece.DRAGON);

		// choose character
		firstPlayer.setCoin(3);
		assertTrue(expertRules.doAction(new Action(ActionType.CHOOSE_CHARACTER,12)));
		game.getPlayerHandler().getCurrentPlayer().registerAction(new Action(ActionType.CHOOSE_CHARACTER));
		// check activation needed
		assertEquals(ActionType.STUDENT_FROM_DINING_TO_BAG, firstPlayer.getActiveCharacter().getRules()
				.nextPossibleActions().getActionsList().get(0).getActionType());
		// invalid activation
		assertFalse(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_DINING_TO_BAG, null)));

		// valid activation (get dragon from dining)
		assertTrue(firstPlayer.getActiveCharacter().getRules().doAction(new Action(ActionType.STUDENT_FROM_DINING_TO_BAG, Piece.DRAGON)));
		assertEquals(numDragon - 1, firstPlayer.getPlayerBoard().getNumOfStudentsRoom(Piece.DRAGON));
	}
}
