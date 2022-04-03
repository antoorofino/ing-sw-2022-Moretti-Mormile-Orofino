package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.PlayerException;

import java.util.ArrayList;

public class PlayersHandler {
    private int numPlayer;
    private ArrayList<Player> players;
    private Player currentPlayer;


    //TODO implement method AddPlayer
    public void addPlayer(Player player){
        this.players.add(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    //TODO implement method getPlayersNickName
    public ArrayList<String> getPlayersNickName(){
        ArrayList<String> niknames = new ArrayList<String>();

        for(Player p : this.players){
            niknames.add(p.getNickname());
        }
        return niknames;
    }

    //TODO implement method getPlayersByNickName
    public Player getPlayersByNickName(String nickname) throws PlayerException {

        for(Player p : this.players){
            if(p.getNickname().equals(nickname))
                return p;
        }
        throw new PlayerException("Cannot found player with this nickname");
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //TODO implement method initialiseCurrentPlayer
    public void initialiseCurrentPlayer(){

    }

    //TODO implement method nextPlayerByOrder
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
