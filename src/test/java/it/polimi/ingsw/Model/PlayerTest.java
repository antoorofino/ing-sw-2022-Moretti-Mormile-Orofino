package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.ActionType;
import it.polimi.ingsw.Controller.RoundActions;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exception.CardException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
	Player player;

	@BeforeEach
	public void setUp() {
		player = new Player();
	}

	@AfterEach
	public void tearDown() {
		player = null;
	}

	@Test
	void setNicknameTest() {
		String nickname = new String("TestNickname");
		player.setNickname(nickname);
		assertEquals(nickname, player.getNickname());
	}

	@Test
	void setPlayerColorTest(){
		PlayerColor color = PlayerColor.BLACK;
		player.setPlayerColor(color);
		assertEquals(color,player.getPlayerColor());
	}

	@Test
	void roundActionTest(){
		Action action1 = new Action(ActionType.MOVE_STUDENT_TO_ISLAND);
		Action action2 = new Action(ActionType.MOVE_STUDENT_TO_DININGROOM);
		RoundActions roundActions = new RoundActions();
		roundActions.add(action1);
		player.setRoundActions(roundActions);
		player.registerAction(action2);
		roundActions.add(action2);
		assertEquals(roundActions,player.getRoundActions());
	}

	@Test
	void TowerPlayerTest(){
		int towers = 1;
		player.setNumOfTower(towers);
		player.removeTower(1);
		player.addTower(1);
		assertEquals(false,player.TowerIsEmpty());
		player.removeTower(1);
		assertEquals(true,player.TowerIsEmpty());
	}

	@Test
	void CharacterUseTest(){
		Character character = new Character("Test","Test description",1,1,new Rules());
		player.setActiveCharacter(character);
		assertEquals(character,player.getActiveCharacter());
	}

	@Test
	void lastCardUsedTest(){
		AssistenceCard assistenceCard = new AssistenceCard(1,1,0);
		ArrayList<AssistenceCard> cards = new ArrayList<AssistenceCard>();
		cards.add(assistenceCard);
		player.addCards(cards);
		assertEquals(false,player.noMoreCards());
		try {
			player.setLastCardUsed(assistenceCard);
		} catch (CardException e) {
			//fail();
		}
		assertEquals(assistenceCard,player.getLastCardUsed());
		assertEquals(true,player.noMoreCards());
		try {
			player.setLastCardUsed(assistenceCard);
		} catch (CardException e) {
			//fail();
		}

	}

	@Test
	public void coinsTest(){
		player.setCoin(0);
		player.addCoin();
		assertEquals(true,player.coinsAreEnough(1));
		player.removeCoin(1);
		assertEquals(false,player.coinsAreEnough(1));
		player.removeCoin(1);
	}

	//TODO check resetRoundActionTest

	@Test
	public void resetRoundActionTest(){
		RoundActions r = new RoundActions();
		player.setRoundActions(r);
		assertEquals(r,player.getRoundActions());
		player.resetRoundAction();
		assertEquals(true,player.getRoundActions() instanceof RoundActions);
		assertEquals(false, player.getRoundActions().equals(r));
	}



}