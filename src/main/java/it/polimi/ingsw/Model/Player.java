package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.RoundActions;
import it.polimi.ingsw.Exception.CardException;


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

	public Player(){
		this.playerBoard = new Board();
		this.lastCardUsed= null;
		this.cards = new ArrayList<AssistenceCard>();
		this.playerCoin = 1;
		this.activeCharacter = null;
		this. roundActions = null;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
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

	public void setLastCardUsed(AssistenceCard lastCardUsed) throws CardException {
		if(!cards.contains(lastCardUsed))
			throw new CardException("Player doesn't have the specific card");
		cards.remove(lastCardUsed);
		this.lastCardUsed = lastCardUsed;
	}

	public boolean noMoreCards() {
		return cards.isEmpty();
	}

	public AssistenceCard getLastCardUsed() {
		return lastCardUsed;
	}

	public void setNumOfTower(int num) {
		this.numOfTower = num;
	}

	public void addTower(int numTower) {
		this.numOfTower+=numTower;
	}

	public void removeTower(int towerToremove) {
		this.numOfTower-=towerToremove;
	}

	public boolean TowerIsEmpty(){
		if(numOfTower<=0)
			return true;
		return false;
	}

	public void setCoin(int numCoins) {
		this.playerCoin = numCoins;
	}

	public boolean coinsAreEnough(int necessaryCoins){
		if(this.playerCoin>necessaryCoins)
			return true;
		return false;
	}

	public void removeCoin(int numCoinsToRemove){
		if(coinsAreEnough(numCoinsToRemove))
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
		this.roundActions.add(action);
	}

	public void resetRoundAction() {
		this.roundActions = new RoundActions();
	}
}
