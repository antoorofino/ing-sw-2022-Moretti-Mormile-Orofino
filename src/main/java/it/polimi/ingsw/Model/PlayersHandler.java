package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.PlayerException;
import java.util.ArrayList;

public class PlayersHandler {
    //TODO: see if it has to be removed
    private int numPlayers;
    private final ArrayList<Player> players;
    private Player currentPlayer;

    public PlayersHandler(){
        this.players = new ArrayList<>();
        this.numPlayers = 0;
        this.currentPlayer = null;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public ArrayList<String> getPlayersNickName(){
        ArrayList<String> nicknames = new ArrayList<String>();

        for(Player p : this.players){
            nicknames.add(p.getNickname());
        }
        return nicknames;
    }


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

    // TODO Check the following methods after the controller implementation
    public void initialiseCurrentPlayer(){

        this.currentPlayer = players.get(0);
        for(int i = 1 ; i < this.players.size() ; i++){
            if(players.get(i).getLastCardUsed().getCardValue() < currentPlayer.getLastCardUsed().getCardValue())
                this.currentPlayer = players.get(i);
        }

    }

    public Player nextPlayerByOrder(){
        return players.get((players.indexOf(currentPlayer)+1)%players.size());
    }


    public Player nextPlayerByAssistance(){
        Player nextPlayerByAssitance;
        int i = 0;
        while(this.players.get(i).equals(this.currentPlayer)){
            i++;
        }
        nextPlayerByAssitance = this.players.get(i);
        for(Player p : this.players){
            if(!p.equals(this.currentPlayer) && !p.equals(this.nextPlayerByAssistance())){
                if(p.getLastCardUsed().getCardValue() < nextPlayerByAssitance.getLastCardUsed().getCardValue())
                    nextPlayerByAssitance = p;
                else if(p.getLastCardUsed().getCardValue() == nextPlayerByAssitance.getLastCardUsed().getCardValue()){
                    if(this.players.indexOf(p) < this.players.indexOf(nextPlayerByAssitance))
                        nextPlayerByAssitance = p;
                }
            }

        }
       return nextPlayerByAssitance;
    }


    public boolean playerWithNoMoreCards(){
        for(Player p : players){
            if(p.noMoreCards())
                return true;
        }
        return false;
    }


}
