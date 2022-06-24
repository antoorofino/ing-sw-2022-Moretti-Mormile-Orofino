package it.polimi.ingsw.model;

import it.polimi.ingsw.server.rules.Rules;
import it.polimi.ingsw.util.*;
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
	}

	@AfterEach
	public void tearDown() {
		player = null;
	}

	@Test
	public void getIdTest(){
		assertEquals("test", player.getId());
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
	void TowerPlayerTest(){
		int towers = 1;
		player.setNumOfTowers(towers);
		player.removeTower(1);
		player.addTower(1);
		assertFalse(player.TowerIsEmpty());
		assertEquals(1,player.getNumOfTowers());
		player.removeTower(1);
		assertTrue(player.TowerIsEmpty());
	}

	@Test
	void CharacterUseTest(){
		Character character = new Character("Test","Test description",1,1,new Rules(new GameModel(new GameListInfo("testGame", GameMode.BASIC,2))));
		player.setActiveCharacter(character);
		assertEquals(character,player.getActiveCharacter());
	}

	@Test
	void lastCardUsedTest(){
		ArrayList<AssistantCard> cards = AssistantCard.createDeck();
		AssistantCard assistantCard = cards.get(0);
		player.addCards(cards);
		assertFalse(player.noMoreCards());
		// play assistantCard
		try {
			player.setLastCardUsed(assistantCard);
		} catch (CardException e) {
			fail();
		}
		assertEquals(assistantCard,player.getLastCardUsed());
		assertFalse(player.noMoreCards());
		assertEquals(9,player.getDeck().size());
		// no more playable
		assertThrows(CardException.class,() -> {
			player.setLastCardUsed(assistantCard);
		});
		// reset last card
		player.resetLastCard();
		assertNull(player.getLastCardUsed());
	}

	@Test
	public void coinsTest(){
		player.setCoin(0);
		player.addCoin();
		assertTrue(player.coinsAreEnough(1));
		player.removeCoin(1);
		assertFalse(player.coinsAreEnough(1));
		player.removeCoin(1);
	}

	@Test
	public void isReadyToPlayTest(){
		assertFalse(player.isReadyToPlay());
		player.setTowerColor(TowerColor.BLACK);
		assertFalse(player.isReadyToPlay());
		player.setNickname("test_nickname");
		assertTrue(player.isReadyToPlay());
	}
}