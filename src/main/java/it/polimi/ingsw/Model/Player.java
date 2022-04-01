package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Player {
    private String nickname;
    //private PlayerColor playerColor;
    private Board playerBoard;
    private ArrayList<AssistenceCard> cards;
    private AssistenceCard lastCardUsed;
    private int numOfTower;
    private int playerCoin;
    private Character activeCharacter;
    //private Action roundAction;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /*
    public void setPlayerColor(PlayerColor color){this.colorPlayer = color;}

    public PlayerColor getPlayerColor(){return this.colorPlayer;}
     */

    public Board getPlayerBoard() {
        return playerBoard;
    }

    //TODO implement method addCrds
    public void addCards(ArrayList<AssistenceCard> cards){}

    public boolean noMoreCards(){return cards.isEmpty();}

    public AssistenceCard getLastCardUsed() {
        return lastCardUsed;
    }

    public void setLastCardUsed(AssistenceCard lastCardUsed) {
        this.lastCardUsed = lastCardUsed;
    }

    public void setNumOfTower(int num) {
        this.numOfTower = num;
    }

    //TODO implement method addTower
    public void addTower(){}

    //TODO implement method removeTower
    public void removeTower(){}

    public void setCoin(int numCoins) {
        this.playerCoin = numCoins;
    }

    public void removeCoin(int numCoinsToRemove){this.playerCoin-=numCoinsToRemove;}

    //TODO implement method addCoin
    public void addCoin(){}

    public Character getActiveCharacter() {
        return activeCharacter;
    }

    public void setActiveCharacter(Character activeCharacter) {
        this.activeCharacter = activeCharacter;
    }

    /*
    public void setRoundActions(RoundActions roundActions){this.roundActions=roundActions;}
    public RoundActions getRoundActions(){return this.roundActions;}
    public void registerAction(Action action){}
     */
}
