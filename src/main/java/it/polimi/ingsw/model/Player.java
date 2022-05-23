package it.polimi.ingsw.model;

import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.CardException;
import it.polimi.ingsw.util.TowerColor;


import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	private final String id;
	private String nickname;
	private TowerColor towerColor;
	private final Board playerBoard;
	private ArrayList<AssistantCard> cards;
	private AssistantCard lastCardUsed;
	private int numOfTower;
	private int playerCoin;
	private Character activeCharacter;
	private RoundActions roundActions;

	public Player(String id){
		this.id = id;
		this.nickname = null;
		this.playerBoard = new Board();
		this.lastCardUsed= null;
		this.cards = new ArrayList<>();
		this.numOfTower = 0;
		this.playerCoin = 0;
		this.activeCharacter = null;
		this.roundActions = new RoundActions();
		this.towerColor = null;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public String getId() {
		return id;
	}

	public void setTowerColor(TowerColor color) {
		this.towerColor = color;
	}

	public TowerColor getTowerColor() {
		return this.towerColor;
	}

	public Board getPlayerBoard() {
		return playerBoard;
	}

	public ArrayList<AssistantCard> getDeck(){
		return new ArrayList<>(cards);
	}

	public void addCards(ArrayList<AssistantCard> cards) {
		this.cards = cards;
	}

	public void setLastCardUsed(AssistantCard lastCardUsed) throws CardException {
		if(!cards.contains(lastCardUsed))
			throw new CardException("Player doesn't have the specific card");
		cards.remove(lastCardUsed);
		this.lastCardUsed = lastCardUsed;
	}

	public boolean noMoreCards() {
		return cards.isEmpty();
	}

	public AssistantCard getLastCardUsed() {
		return lastCardUsed;
	}

	public void setNumOfTower(int num) {
		this.numOfTower = num;
	}

	public int getNumOfTower(){
		return numOfTower;
	}

	public void addTower(int numTower) {
		this.numOfTower+=numTower;
	}

	public void removeTower(int towerToRemove) {
		this.numOfTower-=towerToRemove;
	}

	public boolean TowerIsEmpty(){
		return numOfTower <= 0;
	}

	public void setCoin(int numCoins) {
		this.playerCoin = numCoins;
	}

	public boolean coinsAreEnough(int necessaryCoins){
		return this.playerCoin >= necessaryCoins;
	}

	public void removeCoin(int numCoinsToRemove){
		if(coinsAreEnough(numCoinsToRemove))
			this.playerCoin -= numCoinsToRemove;
	}

	public void addCoin() {
		this.playerCoin+=1;
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

	public boolean isReadyToPlay(){
		return nickname != null && towerColor != null;
	}
}
