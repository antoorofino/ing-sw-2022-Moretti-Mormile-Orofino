package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Stores information about island
 */
public class Island implements Serializable {
    private Map<Piece, Integer> studentsOnIsland;
    private int ID;
    private int size;
    private Player islandOwner;
    private int flagNoInfluence;

    /**
     * Constructor: builds Island
     * @param islandID identifier of island
     */
    public Island(int islandID){
        this.studentsOnIsland = new HashMap<Piece, Integer>();
        for(Piece piece : Piece.values()){
            studentsOnIsland.put(piece,0);
        }
        this.ID=islandID;
        this.islandOwner = null;
        this.size = 1;
        this.flagNoInfluence = 0;
    }

    /**
     * Checks if tower is already build, so if the island has an owner
     * @return true if island has an owner
     */
    public boolean towerIsAlreadyBuild(){
        if(islandOwner == null)
            return false;
        else
            return true;
    }

    /**
     * Gets number of students on island
     * @param s type of student
     * @return number of students on island
     */
    public int getNumStudents(Piece s){
        return studentsOnIsland.get(s);
    }

    /**
     * Adds student on island
     * @param s student to add
     */
    public void addStudent(Piece s){
        studentsOnIsland.put(s,studentsOnIsland.get(s)+1);
    }

    /**
     * Gets island owner
     * @return island owner
     */
    public Player getIslandOwner(){
        return islandOwner;
    }

    /**
     * Gets size of island
     * @return size of island
     */
    public int getSize(){
        return size;
    }

    /**
     * Checks if player has influence on an island
     * @param teacher helper of class teacher used to check if teacher has owner
     * @param towerCount true if tower has influence during calculation of influence
     * @param invalidColor color that has not influence during calculation of influence
     * @param extraPoints player that has extra points
     * @return true if island has owner otherwise return false
     */
    public boolean calculateInfluence(TeachersHandler teacher, boolean towerCount, Piece invalidColor, Player extraPoints){
        int count;
        Player player;
        HashMap<Player, Integer> scores = new HashMap<Player, Integer>();
        if(this.flagNoInfluence>0){
            this.flagNoInfluence--;
            return false;
        }
        // extra points
        if(extraPoints!=null){
            scores.put(extraPoints,2);
        }
        for (Piece piece: Piece.values()) { // assign points for piece color
            if(!piece.equals(invalidColor)){ // if valid
                player = teacher.getTeacherOwner(piece);
                if(player!=null){
                    count = getCount(player,scores);
                    scores.put(player, count + getNumStudents(piece));
                }
            }
        }

        if(towerCount){
            player = getIslandOwner();
            if(player!=null) {
                count = getCount(player,scores);
                scores.put(player, count + getSize());
            }
        }

        boolean tie = false;
        if(scores.size() > 1){
            tie = true;
            Iterator it = scores.entrySet().iterator();
            Map.Entry entry = (Map.Entry)it.next();
            Map.Entry next;
            while(it.hasNext()){
                next = (Map.Entry)it.next();
                if(entry.getValue()!=next.getValue())
                    tie = false;
            }
        }

        if(tie) // tie don't update owner
            return true;

        // find the new owner
        for (Player p : scores.keySet()) {
            player = getIslandOwner();
            if(player != null) {
                if (!p.equals(player) && scores.get(p) > scores.get(player)) {
                    player.addTower(getSize());
                    p.removeTower(getSize());
                    islandOwner = p;
                }
            }else{
                if(scores.get(p)!=0){
                    p.removeTower(getSize());
                    islandOwner = p;
                }
            }
        }
        return true;
    }

    /**
     * Gets player's points of influence
     * @param player player that we want to know influence's points
     * @param map that contains points
     * @return value of points
     */
    public int getCount(Player player,HashMap<Player, Integer> map){
        if (!map.containsKey(player))
            map.put(player, 0);
        return map.get(player);
    }

    /**
     * Gets island's ID
     * @return island's ID
     */
    public int getID(){
        return this.ID;
    }

    /**
     * Adds flag to notify that can't calculate influence on the island
     */
    public void addFlagNoInfluence(){
        this.flagNoInfluence++;
    }

    /**
     * Gets number of flag no influence on island
     * @return number of flag no influence on island
     */
    public int getFlagNoInfluence(){
        return this.flagNoInfluence;
    }

    /**
     * Increases island's size when merge islands
     * @param size how much increase original size
     */
    public void increaseSize(int size){ this.size+=size;}

    /**
     * Decreases ID of island when merge islands
     */
    public void decreaseID(){this.ID--;}

    /**
     * Gets students on island
     * @return map that contains how many of each student's type are on the island
     */
    public Map<Piece, Integer> getStudentsOnIsland(){ return new HashMap<>(studentsOnIsland);}
}
