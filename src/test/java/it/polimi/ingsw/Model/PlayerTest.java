package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.ActionType;
import it.polimi.ingsw.Controller.RoundActions;
import it.polimi.ingsw.Controller.Rules.Rules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	void registerActionTest(){
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
		player.removeTower(1);
		assertEquals(true,player.TowerIsEmpty());
	}

	@Test
	void CharactertUseTest(){
		Character character = new Character("Test","Test description",1,1,new Rules());
		player.setActiveCharacter(character);
		assertEquals(character,player.getActiveCharacter());
	}


}