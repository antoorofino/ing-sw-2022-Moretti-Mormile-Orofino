package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class PlayersHandler {
    private int numPlayer;
    private ArrayList<Player> players;
    private Player currentPlayer;


    //TODO implement method AddPlayer
    public void addPlayer(Player player){}

    public ArrayList<Player> getPlayers() {
        return players;
    }

    //TODO implement method getPlayersNickName
    public ArrayList<String> getPlayersNickName(){
        return null;
    }

    //TODO implement method getPlayersByNickName
    public Player getPlayersByNickName(String nickname){
        return null;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //TODO implement method initialiseCurrentPlayer
    public void initialiseCurrentPlayer(){

    }

    //TODO implement method nectPlayerByOrder
    public Player nextPlayerByOrder(){
        return null;
    }

    //TODO implement method nextPlayerByAssistance
    public Player nextPlayerByAssistance(){
        return null;
    }

    //TODO implement method playerWithNoMoreCards
    public boolean playerWithNoMoreCards(){
        return true;
    }


}
