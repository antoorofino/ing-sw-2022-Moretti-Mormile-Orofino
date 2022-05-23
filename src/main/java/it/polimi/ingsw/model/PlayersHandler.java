package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.PlayerException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class PlayersHandler implements Serializable {
    private int numPlayers;
    private final ArrayList<Player> players;
    private Player currentPlayer;
    private Player firstPlayer;
    private ArrayList<Player> alreadyPlayed;

    public PlayersHandler(){
        this.players = new ArrayList<>();
        this.numPlayers = 0;
        this.currentPlayer = null;
        this.firstPlayer = null;
        this.alreadyPlayed = new ArrayList<>();
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
        ArrayList<String> nicknames = new ArrayList<>();
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

    public void initialiseCurrentPlayerPlanningPhase(){
        if(currentPlayer == null) {
            firstPlayer = players.get(new Random().nextInt(players.size()));
        }else{
            firstPlayer = sortByAssistance(firstPlayer, players).get(0);
        }
        currentPlayer = firstPlayer;
    }

    public void initialiseCurrentPlayerActionPhase(){
        alreadyPlayed = sortByAssistance(firstPlayer, players);
        firstPlayer = alreadyPlayed.get(0);
        currentPlayer = firstPlayer;
    }

    public void nextPlayerByOrder(){
        alreadyPlayed.add(currentPlayer);
        currentPlayer = players.get((players.indexOf(currentPlayer)+1)%players.size());
    }

    public void nextPlayerByAssistance(){
        alreadyPlayed.remove(0);
        try {
            currentPlayer = alreadyPlayed.get(0);
        } catch (IndexOutOfBoundsException ignored){
        }
    }

    private ArrayList<Player> sortByAssistance(Player first, ArrayList<Player> toSort){
        ArrayList<Player> sorted = new ArrayList<>();
        for(int i = toSort.indexOf(first); i < toSort.indexOf(first) + toSort.size(); i++)
            sorted.add(toSort.get(i % toSort.size()));
        for(int j = 0; j < toSort.size(); j++){
            for(int i = 0; i < sorted.size()-1; i++)
                if(sorted.get(i).getLastCardUsed().getCardValue() > sorted.get(i+1).getLastCardUsed().getCardValue()) {
                    Player temp;
                    temp = sorted.get(i);
                    sorted.set(i, sorted.get(i+1));
                    sorted.set(i+1, temp);
                }
        }
        return sorted;
    }

    public List<AssistantCard> cardsAlreadyPlayed(){
        return alreadyPlayed.stream().map(Player::getLastCardUsed).collect(Collectors.toList());
    }

    public boolean playerWithNoMoreCards(){
        for(Player p : players){
            if(p.noMoreCards())
                return true;
        }
        return false;
    }

    public boolean everyPlayerIsReadyToPlay(){
        return numPlayers != 0 && players.stream().filter(Player::isReadyToPlay).count() == numPlayers ;
    }
}
