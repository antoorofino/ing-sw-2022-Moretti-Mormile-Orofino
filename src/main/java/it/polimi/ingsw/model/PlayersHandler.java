package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.PlayerException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper class, manages player
 */
public class PlayersHandler implements Serializable {
    private final int numPlayers;
    private final ArrayList<Player> players;
    private Player currentPlayer;
    private Player firstPlayer;
    private ArrayList<Player> alreadyPlayed;

    /**
     * Constructor: builds players handler
     * @param numPlayers how many players will be in the game
     */
    public PlayersHandler(int numPlayers){
        this.players = new ArrayList<>();
        this.numPlayers = numPlayers;
        this.currentPlayer = null;
        this.firstPlayer = null;
        this.alreadyPlayed = new ArrayList<>();
    }

    /**
     * Adds player in the list of player
     * @param player that will be added in the list
     */
    public void addPlayer(Player player){
        if(numPlayers == players.size())
            throw new IllegalStateException("Game already full: " + numPlayers);
        this.players.add(player);
    }

    /**
     * Gets number of player
     * @return how many players will play in the game
     */
    public int getNumPlayers(){
        return this.numPlayers;
    }

    /**
     * Gets list of players
     * @return list of players
     */
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Gets list of players' nicknames
     * @return list of players' nicknames
     */
    public ArrayList<String> getPlayersNickName(){
        return getPlayers().stream().map(Player::getNickname).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets player with a specifc nickname
     * @param nickname of specific player
     * @return player with specific nickname
     * @throws PlayerException invalid nickname for player
     */
    public Player getPlayersByNickName(String nickname) throws PlayerException {
        for(Player p : this.players){
            if(p.getNickname().equals(nickname))
                return p;
        }
        throw new PlayerException("Cannot found player with nickname " + nickname);
    }

    /**
     * Gets player by ID
     * @param id specific ID of player
     * @return player with specif ID
     * @throws PlayerException invalid id for player
     */
    public Player getPlayerById(String id) throws PlayerException {
        for(Player p : this.players){
            if(p.getId().equals(id))
                return p;
        }
        throw new PlayerException("Cannot found player with id " + id);
    }

    /**
     * Gets player whose turn it's
     * @return player whose turn it's
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Initialises the player will play at first during the turn
     */
    public void initialiseCurrentPlayerPlanningPhase(){
        if(currentPlayer == null) {
            firstPlayer = players.get(new Random().nextInt(players.size()));
        }else{
            firstPlayer = sortByAssistance(firstPlayer, players).get(0);
        }
        currentPlayer = firstPlayer;
    }

    /**
     * Initialises current player order by assistance
     */
    public void initialiseCurrentPlayerActionPhase(){
        alreadyPlayed = sortByAssistance(firstPlayer, players);
        firstPlayer = alreadyPlayed.get(0);
        currentPlayer = firstPlayer;
    }

    /**
     * Sets current player as the first player from ordered list
     */
    public void nextPlayerByOrder(){
        alreadyPlayed.add(currentPlayer);
        currentPlayer = players.get((players.indexOf(currentPlayer)+1)%players.size());
    }

    /**
     * Sets current player as the first player from ordered list by assistance
     */
    public void nextPlayerByAssistance(){
        alreadyPlayed.remove(0);
        try {
            currentPlayer = alreadyPlayed.get(0);
        } catch (IndexOutOfBoundsException ignored){
        }
    }

    /**
     * Returns player in order to play ordered by assistance that every player has chosen
     * @param first first player
     * @param toSort list of player
     * @return sorted list of player
     */
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

    /**
     * Gets the list of cards that player has already played
     * @return the list of cards that player has already played
     */
    public List<AssistantCard> cardsAlreadyPlayed(){
        return alreadyPlayed.stream().map(Player::getLastCardUsed).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Checks if there are any player without cards
     * @return true if a player hasn't cards enough
     */
    public boolean playerWithNoMoreCards(){
        for(Player p : players){
            if(p.noMoreCards())
                return true;
        }
        return false;
    }

    /**
     * Checks if all players in the game are ready to play
     * @return true if all players are ready to play
     */
    public boolean everyPlayerIsReadyToPlay(){
        return players.stream().filter(Player::isReadyToPlay).count() == numPlayers ;
    }
}
