package it.polimi.ingsw.model;

import it.polimi.ingsw.util.Action;
import it.polimi.ingsw.util.RoundActions;
import it.polimi.ingsw.util.exception.CardException;
import it.polimi.ingsw.util.TowerColor;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores information of Player
 */
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

	/**
	 * Constructor: builds player
	 * @param id player's id
	 */
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

	/**
	 * Sets nickname to player
	 * @param nickname the chosen nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Gets player's nickname
	 * @return player's nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Gets player's ID
	 * @return player's ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets color of tower that player will use
	 * @param color the color to be set
	 */
	public void setTowerColor(TowerColor color) {
		this.towerColor = color;
	}

	/**
	 * Gets tower's color that player uses
	 * @return the tower color
	 */
	public TowerColor getTowerColor() {
		return this.towerColor;
	}

	/**
	 * Gets player's board
	 * @return player's board
	 */
	public Board getPlayerBoard() {
		return playerBoard;
	}

	/**
	 * Gets player's deck of assistant card
	 * @return player's deck of assistant card
	 */
	public ArrayList<AssistantCard> getDeck(){
		return new ArrayList<>(cards);
	}

	/**
	 * Add cards in the player's deck
	 * @param cards that will be added to deck
	 */
	public void addCards(List<AssistantCard> cards) {
		this.cards = new ArrayList<>(cards);
	}

	/**
	 * Sets last card that player used
	 * @param lastCardUsed last card that player used
	 * @throws CardException assistant card not found in deck
	 */
	public void setLastCardUsed(AssistantCard lastCardUsed) throws CardException {
		for (AssistantCard card:cards){
			if(card.getCardID() == lastCardUsed.getCardID()){
				cards.remove(card);
				this.lastCardUsed = card;
				return;
			}
		}
		throw new CardException("Player doesn't have the specific card");
	}

	/**
	 * Checks if there are any card in the deck
	 * @return true if there aren't any cards, otherwise return true
	 */
	public boolean noMoreCards() {
		return cards.isEmpty();
	}

	/**
	 * Gets last card that player used
	 * @return the last Assistant card used
	 */
	public AssistantCard getLastCardUsed() {
		return lastCardUsed;
	}

	/**
	 * Sets number of towers that player has got
	 * @param num number of tower
	 */
	public void setNumOfTowers(int num) {
		this.numOfTower = num;
	}

	/**
	 * Gets number of towers that
	 * @return the number of tower owned by player
	 */
	public int getNumOfTowers(){
		return numOfTower;
	}

	/**
	 * Adds towers to player
	 * @param numTower specify how many tower will be added to player
	 */
	public void addTower(int numTower) {
		this.numOfTower+=numTower;
	}

	/**
	 * Removes towers from player
	 * @param towerToRemove specify how many towers will be removed from player
	 */
	public void removeTower(int towerToRemove) {
		this.numOfTower-=towerToRemove;
	}

	/**
	 * Checks if player hasn't any towers
	 * @return true if player hasn't any towers otherwise return false
	 */
	public boolean TowerIsEmpty(){
		return numOfTower <= 0;
	}

	/**
	 * Gives coins to player
	 * @param numCoins how many coins player receives
	 */
	public void setCoin(int numCoins) {
		this.playerCoin = numCoins;
	}

	/**
	 * Checks if player has enough coin to do the action
	 * @param necessaryCoins how many coins are necessary
	 * @return true if player has enough coins, otherwise false
	 */
	public boolean coinsAreEnough(int necessaryCoins){
		return this.playerCoin >= necessaryCoins;
	}

	/**
	 * Player uses coins to activate action
	 * @param numCoinsToRemove how many coins player uses to do action
	 */
	public void removeCoin(int numCoinsToRemove){
		if(coinsAreEnough(numCoinsToRemove))
			this.playerCoin -= numCoinsToRemove;
	}

	/**
	 * Player earns a coin
	 */
	public void addCoin() {
		this.playerCoin+=1;
	}

	/**
	 * Gets how many coins player has
	 * @return the coins owned by player
	 */
	public int getCoin(){ return this.playerCoin;}

	/**
	 * Gets character that player has activated
	 * @return character activated from player
	 */
	public Character getActiveCharacter() {
		return activeCharacter;
	}

	/**
	 * Sets character activated by player
	 * @param activeCharacter charcter activated by player
	 */
	public void setActiveCharacter(Character activeCharacter) {
		this.activeCharacter = activeCharacter;
	}

	/**
	 * Gets action that player did during his turn
	 * @return round action
	 */
	public RoundActions getRoundActions() {
		return this.roundActions;
	}

	/**
	 * Registers an action that player did
	 * @param action that player did
	 */
	public void registerAction(Action action) {
		this.roundActions.add(action);
	}

	/**
	 * Resets round action at the end of turn
	 */
	public void resetRoundAction() {
		this.roundActions = new RoundActions();
	}

	/**
	 * Checks if player is ready to play
	 * @return true if player is ready otherwise false
	 */
	public boolean isReadyToPlay(){
		return nickname != null && towerColor != null;
	}

	/**
	 * Resets lastCardUsed
	 */
	public void resetLastCard(){this.lastCardUsed = null;}
}
