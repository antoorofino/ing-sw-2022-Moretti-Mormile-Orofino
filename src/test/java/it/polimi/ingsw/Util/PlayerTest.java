package it.polimi.ingsw.Util;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.TowerColor;
import it.polimi.ingsw.util.ActionType;
import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.GameListInfo;
import it.polimi.ingsw.util.GameMode;
import it.polimi.ingsw.server.rules.Rules;
import it.polimi.ingsw.util.exception.CardException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
	Player player;

	@BeforeEach
	public void setUp() {
		player = new Player("test");
		assertEquals("test", player.getId());
	}

	@AfterEach
	public void tearDown() {
		player = null;
	}

	@Test
	void setNicknameTest() {
		String nickname = "TestNickname";
		player.setNickname(nickname);
		assertEquals(nickname, player.getNickname());
	}

	@Test
	void setPlayerColorTest(){
		TowerColor color = TowerColor.BLACK;
		player.setTowerColor(color);
		assertEquals(color,player.getTowerColor());
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
		player.setNumOfTowers(towers);
		player.removeTower(1);
		player.addTower(1);
		assertEquals(false,player.TowerIsEmpty());
		player.removeTower(1);
		assertEquals(true,player.TowerIsEmpty());
	}

	@Test
	void CharacterUseTest(){
		Character character = new Character("Test","Test description",1,1,new Rules(new GameModel(new GameListInfo("", GameMode.BASIC,2))));
		player.setActiveCharacter(character);
		assertEquals(character,player.getActiveCharacter());
	}

	@Test
	void lastCardUsedTest(){
		AssistantCard assistantCard = new AssistantCard(1,1,0);
		ArrayList<AssistantCard> cards = new ArrayList<>();
		cards.add(assistantCard);
		player.addCards(cards);
		assertEquals(false,player.noMoreCards());
		try {
			player.setLastCardUsed(assistantCard);
		} catch (CardException e) {
			//fail();
		}
		assertEquals(assistantCard,player.getLastCardUsed());
		assertEquals(true,player.noMoreCards());
		try {
			player.setLastCardUsed(assistantCard);
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