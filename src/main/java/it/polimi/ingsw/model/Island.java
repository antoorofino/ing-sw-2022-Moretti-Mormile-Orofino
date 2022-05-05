package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores information about island
 */
public class Island {
    private Map<Piece, Integer> studentsOnIsland;
    private int ID;
    private int size;
    private Player islandOwner;
    private int flagNoInfluence;

    /**
     * Constructor: build Island
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
        return studentsOnIsland.get(s).intValue();
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
     * Get size of island
     * @return size of island
     */
    public int getSize(){
        return size;
    }

    /**
     * Calculate the influence on teacher
     * @param teacher to have the influence
     * @param towerCount true if towers are important to calculate influence
     * @param invalidColor color that isn't influent
     */

    public void calculateInfluence(TeachersHandler teacher, boolean towerCount, Piece invalidColor, Player extraPoints){
        int count;
        Player player;
        HashMap<Player, Integer> scores = new HashMap<Player, Integer>();
        if(this.flagNoInfluence>0){
            removeFlagNoInfluence();
            return;
        }
        // punteggio extra
        if(extraPoints!=null){
            scores.put(extraPoints,2);
        }
        for (Piece piece: Piece.values()) { // assegno punteggio per pedine colori
            if(!piece.equals(invalidColor)){ // se è valido
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

        // find the new owner
        player = getIslandOwner();
        for (Player p : scores.keySet()) {
            if((player== null) || (!p.equals(player) && scores.get(p)>scores.get(player)))
                player = p;
        }

        if(getIslandOwner()==null || !getIslandOwner().equals(player) ){
            if(getIslandOwner()!=null)
                getIslandOwner().addTower(getSize());
            player.removeTower(getSize());
            islandOwner = player;
        }
    }

    public int getCount(Player player,HashMap<Player, Integer> map){
        if (!map.containsKey(player))
            map.put(player, 0);
        return map.get(player);
    }

    public int getID(){
        return this.ID;
    }

    public void addFlagNoInfluence(){
        this.flagNoInfluence++;
    }

    public void removeFlagNoInfluence(){
        this.flagNoInfluence--;
    }

    public int getFlagNoInfluence(){
        return this.flagNoInfluence;
    }

}
