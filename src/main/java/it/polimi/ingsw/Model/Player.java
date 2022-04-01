package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.RoundActions;

import java.util.ArrayList;

public class Player {
	private String nickname;
	private PlayerColor playerColor;
	private Board playerBoard;
	private ArrayList<AssistenceCard> cards;
	private AssistenceCard lastCardUsed;
	private int numOfTower;
	private int playerCoin;
	private Character activeCharacter;
	private RoundActions roundActions;


	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPlayerColor(PlayerColor color) {
		this.playerColor = color;
	}

	public PlayerColor getPlayerColor() {
		return this.playerColor;
	}

	public Board getPlayerBoard() {
		return playerBoard;
	}

	public void addCards(ArrayList<AssistenceCard> cards) {
		this.cards = cards;
	}

	public boolean noMoreCards() {
		return cards.isEmpty();
	}

	public AssistenceCard getLastCardUsed() {
		return lastCardUsed;
	}

	public void setLastCardUsed(AssistenceCard lastCardUsed) {
		this.lastCardUsed = lastCardUsed;
	}

	public void setNumOfTower(int num) {
		this.numOfTower = num;
	}

	public void addTower() {
		this.numOfTower++;
	}

	public void removeTower(int towerToremove) {
		this.numOfTower-=towerToremove;
	}

	public void setCoin(int numCoins) {
		this.playerCoin = numCoins;
	}

	public void removeCoin(int numCoinsToRemove) {
		this.playerCoin -= numCoinsToRemove;
	}

	public void addCoin() {
		this.playerCoin++;
	}

	public Character getActiveCharacter() {
		return activeCharacter;
	}

	public void setActiveCharacter(Character activeCharacter) {
		this.activeCharacter = activeCharacter;
	}

	public void setRoundActions(RoundActions roundActions) {
		this.roundActions = roundActions;
	}

	public RoundActions getRoundActions() {
		return this.roundActions;
	}

	public void registerAction(Action action) {
	}
}
